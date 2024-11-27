package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;

    public Bid insert(Bid bid) {
        Bid insertedBid = bidRepository.insert(bid);
        Bid updatedBid = bidRepository.update(insertedBid);
        return updatedBid;
    }

    public List<Bid> selectAll() {
        return bidRepository.selectAll();
    }
}
