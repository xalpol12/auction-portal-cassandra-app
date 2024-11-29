package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping()
    public ResponseEntity<Auction> insert(@Valid @RequestBody Auction.AuctionInput auctionInput) {
        Auction result = auctionService.insert(auctionInput);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Auction>> selectAll() {
        return ResponseEntity.ok(auctionService.selectAll());
    }
}
