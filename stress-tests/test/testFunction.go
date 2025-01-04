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

	return sendRequest(&user, utils.UsersAddr)
}

func InitAuction(auctionName string) *model.Auction {
	auction := model.Auction{AuctionName: auctionName, StartDate: utils.FormatDate(time.Now()), EndDate: utils.FormatDate(time.Now().Add(time.Second * 5)), StartPrice: float64(1)}

	return sendRequest(&auction, utils.AuctionsAddr)
}

func PutBid(auctionId string, userId string, bidValue float64) *model.Bid {
	bid := model.Bid{AuctionId: auctionId, UserId: userId, BidValue: bidValue}

	return sendRequest(&bid, utils.BidsAddr)
}

func sendRequest[T any](v *T, addr string) *T {
	requestBody, err := json.Marshal(v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during json parsing: %w", err))
	}
	response, err := http.Post(addr, "application/json", bytes.NewBuffer(requestBody))
	if err != nil {
		fmt.Println(fmt.Errorf("failure during http post: %w", err))
	}
	defer response.Body.Close()

	if response.StatusCode != 200 {
		body, _ := io.ReadAll(response.Body)
		fmt.Println(fmt.Errorf("received non 200 status code: %d, message: %w", response.StatusCode, string(body)))
	}

	responseBody, err := io.ReadAll(response.Body)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during read response: %w", err))
	}

	err = json.Unmarshal(responseBody, v)
	if err != nil {
		fmt.Println(fmt.Errorf("failure during unmarshal response: %w", err))
	}

	return v
}
