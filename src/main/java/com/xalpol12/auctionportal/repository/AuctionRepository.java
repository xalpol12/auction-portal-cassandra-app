package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.repository.mappers.CassandraMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.ResultSet;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class AuctionRepository {
    private final static String TABLE_NAME = "AUCTIONS";
    private final Session session;
    private final CassandraMapper<Auction> auctionMapper;

    private PreparedStatement INSERT_INTO_AUCTIONS;

    public List<Auction> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<Auction> auctions = new ArrayList<>();

        result.forEach(x -> auctions.add(auctionMapper.map(x)));
        return auctions;
    }

    public Auction insert(Auction auction) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_AUCTIONS);
        auction.setId(UUID.randomUUID());
        bsInsert.bind(
                auction.getId(),
                auction.getStartDate(),
                auction.getEndDate(),
                auction.getAuctionName(),
                auction.getStartPrice()
        );
        ResultSet insertResult = session.execute(bsInsert);
        if (insertResult.wasApplied()) {
            return auction;
        } else {
            throw new RuntimeException("Insert failed");
        }
    }

    @PostConstruct
    private void init() {
        INSERT_INTO_AUCTIONS = session.prepare(auctionMapper.getInsertStatement(TABLE_NAME));
    }
}
