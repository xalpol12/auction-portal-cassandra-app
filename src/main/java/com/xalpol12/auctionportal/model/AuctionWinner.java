package com.xalpol12.auctionportal.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record AuctionWinner (
        LocalDateTime startDate,
        LocalDateTime endDate,
        String auctionName,
        BigDecimal startPrice,
        String winningUsername,
        BigDecimal winningValue,
        LocalDateTime winningBidTime
) {

    private static AuctionWinnerBuilder getBuilder(Auction auction) {
        return AuctionWinner.builder()
                .startDate(auction.getStartDate())
                .endDate(auction.getEndDate())
                .auctionName(auction.getAuctionName())
                .startPrice(auction.getStartPrice());
    }

    public static AuctionWinner map(Auction auction) {
        return AuctionWinner.getBuilder(auction).build();
    }

    public static AuctionWinner map(Auction auction, User user, Bid bid) {
        return AuctionWinner.getBuilder(auction)
                .winningUsername(user.getName())
                .winningValue(bid.getBidValue())
                .winningBidTime(bid.getBidTime())
                .build();
    }

    public static AuctionWinner map(Auction auction, Bid bid) {
        Optional<Bid> possibleBid = Optional.ofNullable(bid);
        if (possibleBid.isPresent()) {
            return AuctionWinner.getBuilder(auction)
                    .winningValue(bid.getBidValue())
                    .build();
        }
        return AuctionWinner.getBuilder(auction)
                .winningValue(auction.getStartPrice())
                .build();
    }
}
