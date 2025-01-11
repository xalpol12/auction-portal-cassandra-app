package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.model.AuctionWinner;
import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.repository.AuctionRepository;
import com.xalpol12.auctionportal.repository.BidRepository;
import com.xalpol12.auctionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public AuctionWinner getAuctionById(UUID auctionId) {
        Auction auction = auctionRepository.selectById(auctionId);
        List<Bid> bids = bidRepository.selectAllByAuctionId(auctionId);
        Bid winningBid = null;
        if (!CollectionUtils.isEmpty(bids)) {
            bids.sort(Comparator.comparing(Bid::getBidValue).thenComparing(Bid::getBidTime));
            winningBid = bids.getFirst();
        }
        if (auction.getEndDate().isBefore(LocalDateTime.now())) {
            User winner = userRepository.selectById(winningBid != null ? winningBid.getUserId() : null);
            return AuctionWinner.map(auction, winner, winningBid);
        }
        return AuctionWinner.map(auction, winningBid);
    }

    public Auction insert(Auction.AuctionInput auction) {
        return auctionRepository.insert(auction);
    }

    public List<Auction> selectAll() {
        return auctionRepository.selectAll();
    }
}
