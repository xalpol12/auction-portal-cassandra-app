package main

import (
	"fmt"
	"os"
	"os/exec"
	"runtime"
	"stress-tests/model"
	"stress-tests/test"
	"stress-tests/utils"
	"sync"
	"time"
)

func main() {
	test.WipeDB()

	users := []string{"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna",
		"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna",
		"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna",
		"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna",
		"Tadeusz", "Marek", "Hieronim", "Anastazja", "Genowefa", "Krystyna",
	}
	var createdUsers = make([]*model.User, 0)
	for _, user := range users {
		createdUser := test.InitUser(user)
		createdUsers = append(createdUsers, createdUser)
	}

	fmt.Println(createdUsers)

	auctions := []string{"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
		"Zaliczenia z SWN", "Gotowe projekty z Cassandry", "Przerobione SLR", "Tytuł magistra", "Sprzedam Opla, tanio",
	}

	createdAuctions := make([]*model.Auction, 0)
	for _, auction := range auctions {
		createdAuctions = append(createdAuctions, test.InitAuction(auction, 33))
	}

	var wg sync.WaitGroup

	time.Sleep(3 * time.Second)

	for _, realUser := range createdUsers {
		for _, realAuction := range createdAuctions {
			wg.Add(1)
			go sendRequest(realAuction.Id, realUser.Id, float64(500), &wg)
			time.Sleep(200 * time.Millisecond)
		}
	}

	wg.Wait()

	time.Sleep(5 * time.Second)

	clearTerminal()

	for _, endedAuction := range createdAuctions {
		winningAuction := model.AuctionWinner{}
		test.GetFromRest(utils.AuctionsAddr+"/"+endedAuction.Id, &winningAuction)
		winningAuction.Print()
	}

}

func clearTerminal() {
	switch runtime.GOOS {
	case "windows":
		cmd := exec.Command("cmd", "/c", "cls")
		cmd.Stdout = os.Stdout
		err := cmd.Run()
		if err != nil {
			fmt.Print("\033[H\033[2J") // Fallback to ANSI escape codes
		}
	default: // Linux, macOS, etc.
		fmt.Print("\033[H\033[2J\033[3J")
	}
}

func sendRequest(auctionId, userId string, highestAllowedBid float64, wg *sync.WaitGroup) {
	defer wg.Done()
	test.PutBid(auctionId, userId, highestAllowedBid)
}
