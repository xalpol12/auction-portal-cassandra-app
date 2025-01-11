package com.xalpol12.auctionportal.service;

import com.datastax.driver.core.Session;
import com.xalpol12.auctionportal.dbconnector.CassandraConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceTestService {
    private final CassandraConnector connector;

    public void wipeDb() {
        connector.setUpDb();
    }
}
