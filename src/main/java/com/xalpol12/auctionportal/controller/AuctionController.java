package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.model.AuctionWinner;
import com.xalpol12.auctionportal.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<AuctionWinner> selectById(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }
}
