package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.model.AuctionWinner;
import com.xalpol12.auctionportal.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    //TODO: Dodać endpoint do czyszczenia bazy albo poszczególnych tabel

    @PostMapping()
    public ResponseEntity<Auction> insert(@Valid @RequestBody Auction.AuctionInput auctionInput) {
        log.info("Starting POST call on /auctions, body: {}", auctionInput);
        Auction result = auctionService.insert(auctionInput);
        log.info("Ending POST call on /auctions, result: {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Auction>> selectAll() {
        log.info("Performing GET call on /auctions");
        log.info("TOTAL BID CALLS: {}", BidController.COUNTER);
        return ResponseEntity.ok(auctionService.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionWinner> selectById(@PathVariable UUID id) {
        log.info("Performing GET on /auctions/{}", id);
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }
}
