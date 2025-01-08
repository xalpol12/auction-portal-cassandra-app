package main

import (
	"fmt"
	"stress-tests/model"
	"stress-tests/test"
	"sync"
	"time"
)

// insert danych potrzebnych do testów
// odpalenie gorutyn na /bids i wrzucanie bidów do końca aukcji - aż nie dostanie 500

func main() {
	users := []string{"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna"}
	var createdUsers = make([]model.User, 0)
	for _, user := range users {
		createdUser := test.InitUser(user)
		createdUsers = append(createdUsers, *createdUser)
	}

	fmt.Println(createdUsers)

	auction := test.InitAuction("Sprzedam obraz Słynnego Austriackiego Akwarelisty")
	fmt.Println(auction)
	response := make(chan *model.Bid)

	var wg sync.WaitGroup

	for i, realUser := range createdUsers {
		wg.Add(1)
		go sendRequest(auction.Id, realUser.Id, float64(20+i), response, &wg)
		time.Sleep(100 * time.Millisecond)
	}

	// TODO: Pobieranie najwyższego bida i podbijanie przed wysłaniem, logowanie jeśli się nie uda -> odczytało starą wartość lub ktoś już wrzucił bida

	for range createdUsers {
		fmt.Println(<-response)
	}

	//bid := test.PutBid(auction.Id, createdUsers[0].Id, float64(20))

	//fmt.Println(bid)
}

func sendRequest(auctionId, userId string, bidValue float64, response chan<- *model.Bid, wg *sync.WaitGroup) {
	defer wg.Done()
	response <- test.PutBid(auctionId, userId, bidValue)
}
