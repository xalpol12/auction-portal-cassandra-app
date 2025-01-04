package model

type User struct {
	Id       string   `json:"id,omitempty"`
	Name     string   `json:"name,omitempty"`
	Auctions []string `json:"auctions,omitempty"`
}

type UserInput struct {
	Name string `json:"name,omitempty"`
}

type Auction struct {
	Id          string  `json:"id,omitempty"`
	StartDate   string  `json:"startDate,omitempty"`
	EndDate     string  `json:"endDate,omitempty"`
	AuctionName string  `json:"auctionName,omitempty"`
	StartPrice  float64 `json:"startPrice,omitempty"`
}

type Bid struct {
	AuctionId string  `json:"auctionId,omitempty"`
	Id        string  `json:"id,omitempty"`
	UserId    string  `json:"userId,omitempty"`
	BidValue  float64 `json:"bidValue,omitempty"`
	BidTime   string  `json:"bidTime,omitempty"`
}

type AuctionWinner struct {
	StartDate       string  `json:"startDate,omitempty"`
	EndDate         string  `json:"endDate,omitempty"`
	AuctionName     string  `json:"auctionName,omitempty"`
	StartPrice      float64 `json:"startPrice,omitempty"`
	WinningUsername string  `json:"winningUsername,omitempty"`
	WinningValue    float64 `json:"winningValue,omitempty"`
	WinningBidTime  string  `json:"winningBidTime,omitempty"`
}
