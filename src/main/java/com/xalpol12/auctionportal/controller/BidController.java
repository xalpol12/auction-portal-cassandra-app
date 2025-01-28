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
    static int COUNTER = 0;

    @PostMapping("bids")
    ResponseEntity<Bid> addBid(@Valid @RequestBody Bid.BidInput bidInput) {
        Bid result = bidService.insert(bidInput);
        COUNTER++;
        return ResponseEntity.ok(result);
    }

    @GetMapping("bids")
    ResponseEntity<List<Bid>> getAllBids() {
        log.info("Performing GET cal on /bids");
        return ResponseEntity.ok(bidService.selectAll());
    }
}
