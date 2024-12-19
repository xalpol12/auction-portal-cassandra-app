package main

import (
	"fmt"
	"stress-tests/model"
	"stress-tests/test"
)

// insert danych potrzebnych do testów
// odpalenie gorutyn na /bids i wrzucanie bidów do końca aukcji - aż nie dostanie 500

func main() {
	users := []string{"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna"}
	var createdUsers = make([]model.User, len(users))
	for _, user := range users {
		createdUser := test.InitUser(user)
		createdUsers = append(createdUsers, *createdUser)
		fmt.Println(createdUser)
	}

	auction := test.InitAuction("Sprzedam obraz Słynnego Austriackiego Akwarelisty")
	fmt.Println(auction)
	
	bid := test.PutBid(auction.Id, createdUsers[0].Id, float64(20))

	fmt.Println(bid)
}
