package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.Bid;
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

    public Bid insert(Bid bid) {
        Bid insertedBid = bidRepository.insert(bid);
        Bid updatedBid = bidRepository.update(insertedBid);
        userRepository.addAuction(bid.getAuctionId(), bid.getUserId());
        return updatedBid;
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
