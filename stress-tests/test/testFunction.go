package test

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"stress-tests/model"
	"stress-tests/utils"
	"time"
)

func InitUser(name string) *model.User {
	user := model.User{Name: name}
	userResponse, _ := sendRequest(&user, utils.UsersAddr)

	return userResponse
}

func InitAuction(auctionName string) *model.Auction {
	auction := model.Auction{AuctionName: auctionName, StartDate: utils.FormatDate(time.Now()), EndDate: utils.FormatDate(time.Now().Add(time.Second * 5)), StartPrice: float64(1)}

	auctionResponse, _ := sendRequest(&auction, utils.AuctionsAddr)
	return auctionResponse
}

func PutBid(auctionId string, userId string, bidValue float64) *model.Bid {
	bid := model.Bid{AuctionId: auctionId, UserId: userId, BidValue: bidValue}

	highestBid := getHighestBid(auctionId)

	bid.BidValue = highestBid + 1

	responseBid, code := sendRequest(&bid, utils.BidsAddr)

	switch code {
	case 200:
		return responseBid
	case 402:
		//fmt.Println("Mismatched")
		return PutBid(auctionId, userId, bidValue+1)
	}
	return nil
}

func getHighestBid(auctionId string) float64 {
	var responseBid *model.AuctionWinner
	responseBid = getFromRest[model.AuctionWinner](utils.AuctionsAddr + "/" + auctionId)
	return responseBid.WinningValue
}

func sendRequest[T any](v *T, addr string) (*T, int) {
	requestBody, err := json.Marshal(v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during json parsing: %w", err))
	}
	response, err := http.Post(addr, "application/json", bytes.NewBuffer(requestBody))
	if err != nil {
		fmt.Println(fmt.Errorf("failure during http post: %w", err))
	}
	defer response.Body.Close()

	responseBody, err := io.ReadAll(response.Body)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during read response: %w", err))
	}

	err = json.Unmarshal(responseBody, v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during unmarshal response: %w", err))
	}

	return v, response.StatusCode
}

func getFromRest[T any](url string) *T {
	response, err := http.Get(url)
	if err != nil {
		if err != nil {
			fmt.Println(fmt.Errorf("failure during http post: %w", err))
		}
	}
	defer response.Body.Close()

	if response.StatusCode != 200 {
		fmt.Printf("Code different than 200: %d\n", response.StatusCode)
	}

	responseBody, err := io.ReadAll(response.Body)
	if err != nil {
		if err != nil {
			fmt.Println(fmt.Errorf("failure during read response: %w", err))
		}
	}
	var v *T
	err = json.Unmarshal(responseBody, v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during unmarshal response: %w", err))
	}

	return v
}
