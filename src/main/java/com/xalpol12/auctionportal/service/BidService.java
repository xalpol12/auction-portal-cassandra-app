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

    public Bid insert(Bid.BidInput bidInput) {
        // TODO: Probably add some null-checks
        // Get auction for context
        Auction auction = auctionRepository.selectById(bidInput.auctionId());
        // Fetch other bids to decide if bid is even high enough
        List<Bid> bids = bidRepository.selectAllByAuctionId(bidInput.auctionId());
        // Action if there are no other bids
        if (bids.isEmpty()) {
            if (bidInput.bidValue().compareTo(auction.getStartPrice()) != -1) {
                throw new RuntimeException("Bid too low!");
            }
            // Action if there are other bids
        } else if (bids.getFirst().getBidValue().compareTo(bidInput.bidValue()) != -1) {
            throw new RuntimeException("Bid too low!");
        }
        // Insert new bid
        Bid insertedBid = bidRepository.insert(bidInput);
        // Check if bid was placed at the right time
        if (insertedBid.getBidTime() < auction.getEndDate() && insertedBid.getBidTime() > auction.getStartDate()) {
            // Update bid validity if possible
            Bid updatedBid = bidRepository.update(insertedBid);
            // Add auction to user
            userRepository.addAuction(bidInput.auctionId(), bidInput.userId());
            return updatedBid;
        } else {
            throw new RuntimeException("Bid placed to late!");
        }
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
