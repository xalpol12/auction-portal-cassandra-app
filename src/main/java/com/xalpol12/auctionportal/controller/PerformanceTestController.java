package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.service.PerformanceTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/tests")
@RequiredArgsConstructor
public class PerformanceTestController {
    private final PerformanceTestService performanceTestService;

    @DeleteMapping
    public ResponseEntity<Void> wipeDb() {
        log.info("Wiping out DB tables");
        performanceTestService.wipeDb();
        log.info("DB tables wiped out");
        return ResponseEntity.noContent().build();
    }

}
