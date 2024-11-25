package com.xalpol12.auctionportal.repository.mappers;

import com.xalpol12.auctionportal.model.Bid;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.model.enums.BidValidity;
import org.springframework.stereotype.Service;

@Service
public class BidMapper implements CassandraMapper<Bid>{

    @Override
    public Bid map(Row row){
        return Bid.builder()
                .auctionId(row.getUUID("auction_id"))
                .id(row.getUUID("id"))
                .userId(row.getUUID("user_id"))
                .bidValue(row.getDecimal("bid_value"))
                .bidTime(row.getTimestamp("bid_timestamp").getTime())
                .bidValidity(BidValidity.valueOf(row.getString("bid_validity")))
                .build();
    }

    @Override
    public String getInsertStatement(String tableName) {
        return new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (auction_id, id, user_id, bid_value, bid_timestamp, bid_validity) ")
                .append("VALUES ")
                .append("(?, ?, ?, ?, ?, ?)")
                .toString();
    }
}
