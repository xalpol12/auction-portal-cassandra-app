package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.xalpol12.auctionportal.model.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BidRepository {
    private static final String TABLE_NAME = "BIDS";
    private final Session session;

    public List<BidRepository> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<Bid> bids = new ArrayList<>();

        result.forEach(x -> result.add(map(x)
        ));


        return List.of();
    };

    public UUID insert(Bid bid) {

        return null;
    };

    private Bid map(Row row) {
        return Bid.builder()
                .auctionId()
                .id()
                .userId()
                .bidValue()
                .bidTime()
                .bidValidity()
                .build();
    }
}
