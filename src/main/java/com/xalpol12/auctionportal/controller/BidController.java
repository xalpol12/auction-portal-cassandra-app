package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("bids")
    ResponseEntity<Bid> addBid(@Valid @RequestBody Bid.BidInput bidInput) {
        log.info("Starting POST call on /bids, body: {}", bidInput);
        Bid result = bidService.insert(bidInput);
        log.info("Ending POST call on /bids, result: {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("bids")
    ResponseEntity<List<Bid>> getAllBids() {
        log.info("Performing GET cal on /bids");
        return ResponseEntity.ok(bidService.selectAll());
    }
}
