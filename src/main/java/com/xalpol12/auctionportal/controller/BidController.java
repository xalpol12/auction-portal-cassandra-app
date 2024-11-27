package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.repository.BidRepository;
import com.xalpol12.auctionportal.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("bids")
    ResponseEntity<Bid> addBid(@RequestBody Bid bid) {
        return ResponseEntity.ok(bidService.insert(bid));
    }

    @GetMapping("bids")
    ResponseEntity<List<Bid>> getAllBids() {
        return ResponseEntity.ok(bidService.selectAll());
    }
}
