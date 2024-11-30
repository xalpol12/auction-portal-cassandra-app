package com.xalpol12.auctionportal.repository.mappers;

import com.xalpol12.auctionportal.model.Bid;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.utils.DateToLocalDateTime;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class BidMapper implements CassandraMapper<Bid, Bid.BidInput>{

    @Override
    public Bid map(Row row){
        return Bid.builder()
                .auctionId(row.getUUID("auction_id"))
                .id(row.getUUID("id"))
                .userId(row.getUUID("user_id"))
                .bidValue(row.getDecimal("bid_value"))
                .bidTime(DateToLocalDateTime.toLocalDateTime(row.getTimestamp("bid_timestamp")))
                .build();
    }

    @Override
    public Bid map(Bid.BidInput bidInput) {
        return Bid.builder()
                .auctionId(bidInput.auctionId())
                .id(UUID.randomUUID())
                .userId(bidInput.userId())
                .bidValue(bidInput.bidValue())
                .bidTime(LocalDateTime.now())
                .build();

    }

    @Override
    public String getInsertStatement(String tableName) {
        return "INSERT INTO " +
                tableName +
                " (auction_id, id, user_id, bid_value, bid_timestamp) " +
                "VALUES " +
                "(?, ?, ?, ?, ?)";
    }
}
