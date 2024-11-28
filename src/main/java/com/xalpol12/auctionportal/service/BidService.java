package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.repository.AuctionRepository;
import com.xalpol12.auctionportal.repository.BidRepository;
import com.xalpol12.auctionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    public Bid insert(Bid bid) {
        // TODO: Probably add some null-checks
        // Get auction for context
        Auction auction = auctionRepository.selectById(bid.getAuctionId());
        // Fetch other bids to decide if bid is even high enough
        List<Bid> bids = bidRepository.selectAllByAuctionId(bid.getAuctionId());
        // Action if there are no other bids
        if (bids.isEmpty()) {
            if (bid.getBidValue().compareTo(auction.getStartPrice()) != -1) {
                throw new RuntimeException("Bid too low!");
            }
            // Action if there are other bids
        } else if (bids.getFirst().getBidValue().compareTo(bid.getBidValue()) != -1) {
            throw new RuntimeException("Bid too low!");
        }
        // Insert new bid
        Bid insertedBid = bidRepository.insert(bid);
        // Check if bid was placed at the right time
        if (bid.getBidTime() < auction.getEndDate() && bid.getBidTime() > auction.getStartDate()) {
            // Update bid validity if possible
            Bid updatedBid = bidRepository.update(insertedBid);
            // Add auction to user
            userRepository.addAuction(bid.getAuctionId(), bid.getUserId());
            return updatedBid;
        } else {
            throw new RuntimeException("Bid placed to late!");
        }
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
