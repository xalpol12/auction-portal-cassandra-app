### Auction
| id   | start_date | end_date  | bids        | auction_name | start_price | status              | auction_winner |
| ---- | ---------- | --------- | ----------- | ------------ | ----------- | ------------------- | -------------- |
| uuid | timestamp | timestamp | listof(Bid) | varchar      | BigDecimal  | Enum(AuctionStatus) | User           |

partition key (status)
clustering key (auction_name, end_date)

#### AuctionStatus
```js
Enum AuctionStatus {
    Planned,
    Started,
    Finished
}
```

### Bid
```js
Class Bid {
    id: uuid,
    user_id: number,
    bid_value: BigDecimal,
    bid_timestamp: timestamp,
}
```

### User
| id   | username | auctions        |
| ---- | -------- | --------------- |
| uuid | varchar  | Listof(auction) |

partition key (id)
clustering key (username)


- Otwarcie aukcji -> zapisywanie na replice start_date NOW lub data przyszła oraz długość aukcji / data zakończenia w pole end_date
- Użytkownik dołącza do aukcji jeśli ma status otwarty i czas pomiędzy start a end date
- Zakłady przyjmowane dopóki czas przed end_date
- Last write wins + wyższy bid niż poprzedni bid