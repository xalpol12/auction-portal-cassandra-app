package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.service.BidService;
import jakarta.validation.Valid;
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
    ResponseEntity<Bid> addBid(@Valid @RequestBody Bid.BidInput bidInput) {
        return ResponseEntity.ok(bidService.insert(bidInput));
    }

    @GetMapping("bids")
    ResponseEntity<List<Bid>> getAllBids() {
        return ResponseEntity.ok(bidService.selectAll());
    }
}
