package utils

import "time"

var (
	ServiceAddress = "http://localhost:8080/api/v1/"
	UsersAddr      = ServiceAddress + "users"
	BidsAddr       = ServiceAddress + "bids"
	AuctionsAddr   = ServiceAddress + "auctions"
)

func FormatDate(date time.Time) string {
	return date.Format("2006-01-02T15:04:05.999999999")
}
