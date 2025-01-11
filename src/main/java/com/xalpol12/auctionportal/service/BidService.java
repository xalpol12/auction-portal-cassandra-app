package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.exception.BidOutOfTimeException;
import com.xalpol12.auctionportal.exception.BidTooLowException;
import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.repository.AuctionRepository;
import com.xalpol12.auctionportal.repository.BidRepository;
import com.xalpol12.auctionportal.repository.UserRepository;
import com.xalpol12.auctionportal.repository.mappers.BidMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidMapper bidMapper;

    public Bid insert(Bid.BidInput bidInput) {
        // Get auction for context
        Auction auction = auctionRepository.selectById(bidInput.auctionId());
        // Fetch other bids to decide if bid is even high enough
        List<Bid> bids = bidRepository.selectAllByAuctionId(bidInput.auctionId());

        boolean isBidTooLow = false;
        if (bids.isEmpty()) {
            if (bidInput.bidValue().compareTo(auction.getStartPrice()) != 1) {
                isBidTooLow = true;
            }
            // Action if there are other bids
        } else if (bids.getFirst().getBidValue().compareTo(bidInput.bidValue()) != -1) {
            isBidTooLow = true;
        }
        // Insert new bid
        Bid bid = bidMapper.map(bidInput);
        boolean isInvalidBidTime = isInvalidBidTime(auction, bid);

        if (!isInvalidBidTime && !isBidTooLow) {
            return bidRepository.insert(bid);
        } else if (isInvalidBidTime) {
            throw new BidOutOfTimeException("Bid placed too early or too late!");
        } else {
            throw new BidTooLowException("Bid too low!");
        }
    }

    public boolean isInvalidBidTime(Auction auction, Bid bid) {
        return !(bid.getBidTime().isBefore(auction.getEndDate()) && bid.getBidTime().isAfter(auction.getStartDate()));
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
