package test

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
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

func InitAuction(auctionName string, auctionTime int) *model.Auction {
	auction := model.Auction{AuctionName: auctionName, StartDate: utils.FormatDate(time.Now()), EndDate: utils.FormatDate(time.Now().Add(time.Duration(auctionTime) * time.Second)), StartPrice: float64(1)}

	auctionResponse, _ := sendRequest(&auction, utils.AuctionsAddr)
	return auctionResponse
}

func PutBid(auctionId string, userId string, highestAllowedBid float64) {
	bid := model.Bid{AuctionId: auctionId, UserId: userId}

	for {
		highestBid := getHighestBid(auctionId)

		bid.BidValue = highestBid + 1

		if bid.BidValue > highestAllowedBid {
			break
		}

		_, code := sendRequest(&bid, utils.BidsAddr)

		switch code {
		case 200:
			continue
		case 402:
			continue
		case 406:
			log.Printf("Auction has ended")
			return
		}
	}
}

func getHighestBid(auctionId string) float64 {
	responseBid := model.AuctionWinner{}
	GetFromRest(utils.AuctionsAddr+"/"+auctionId, &responseBid)
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

func GetFromRest[T any](url string, v *T) {
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
	err = json.Unmarshal(responseBody, v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during unmarshal response: %w", err))
	}
}

func WipeDB() {
	req, err := http.NewRequest(http.MethodDelete, utils.Tests, nil)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during http delete: %w", err))
	}
	client := &http.Client{}

	_, err1 := client.Do(req)
	if err1 != nil {
		fmt.Println(fmt.Errorf("failure during http delete: %w", err1))
	}
}
