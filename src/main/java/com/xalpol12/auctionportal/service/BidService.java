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
        // Action if there are no other bids
        if (bids.isEmpty()) {
            if (bidInput.bidValue().compareTo(auction.getStartPrice()) != 1) {
                throw new BidTooLowException("Bid too low!");
            }
            // Action if there are other bids
        } else if (bids.getFirst().getBidValue().compareTo(bidInput.bidValue()) != -1) {
            throw new BidTooLowException("Bid too low!");
        }
        // Insert new bid
        Bid bid = bidMapper.map(bidInput);
        // Check if bid was placed at the right time
        if (bid.getBidTime().isBefore(auction.getEndDate()) && bid.getBidTime().isAfter(auction.getStartDate())) {
            return bidRepository.insert(bid);
        } else {
            throw new BidOutOfTimeException("Bid placed too early or too late!");
        }
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
