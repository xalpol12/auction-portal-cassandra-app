package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public Auction insert(Auction auction) {
        return auctionRepository.insert(auction);
    }

    public List<Auction> selectAll() {
        return auctionRepository.selectAll();
    }
}
