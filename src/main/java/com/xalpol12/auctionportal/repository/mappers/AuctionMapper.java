package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.model.Auction;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuctionMapper implements CassandraMapper<Auction, Auction.AuctionInput> {

    @Override
    public Auction map(Row row) {
        return Auction.builder()
                .id(row.getUUID("id"))
                .startDate(row.getTimestamp("start_date").getTime())
                .endDate(row.getTimestamp("end_date").getTime())
                .auctionName(row.getString("auction_name"))
                .startPrice(row.getDecimal("start_price"))
                .build();
    }

    @Override
    public Auction map(Auction.AuctionInput auctionInput) {
        return Auction.builder()
                .id(UUID.randomUUID())
                .startDate(auctionInput.startDate())
                .endDate(auctionInput.endDate())
                .auctionName(auctionInput.auctionName())
                .startPrice(auctionInput.startPrice())
                .build();
    }

    @Override
    public String getInsertStatement(String tableName) {
        return "INSERT INTO " +
                tableName +
                " (id, start_date, end_date, auction_name, start_price) " +
                "VALUES " +
                "(?, ?, ?, ?, ?)";
    }
}
