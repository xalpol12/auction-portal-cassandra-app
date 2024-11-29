package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.xalpol12.auctionportal.model.Auction;
import com.xalpol12.auctionportal.repository.mappers.CassandraMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.querybuilder.Select;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class AuctionRepository {
    private final static String TABLE_NAME = "AUCTIONS";
    private final Session session;
    private final CassandraMapper<Auction, Auction.AuctionInput> auctionMapper;

    private PreparedStatement INSERT_INTO_AUCTIONS;

    public List<Auction> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<Auction> auctions = new ArrayList<>();

        result.forEach(x -> auctions.add(auctionMapper.map(x)));
        return auctions;
    }

    public Auction insert(Auction.AuctionInput auctionInput) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_AUCTIONS);
        Auction auction = auctionMapper.map(auctionInput);
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

    public Auction selectById(UUID auctionID) {
        Select.Where select = QueryBuilder.select().all().from(TABLE_NAME).where(QueryBuilder.eq("id", auctionID));
        ResultSet result = session.execute(select);
        var optional = Optional.ofNullable(result.one());
        if (optional.isPresent()) {
            return auctionMapper.map(result.one());
        } else {
            throw new RuntimeException("Select failed");
        }
    }

    public List<Auction> selectAllByIds(List<UUID> ids) {
        Select.Where select = QueryBuilder.select().all().from(TABLE_NAME).where(QueryBuilder.in("id", ids));
        ResultSet result = session.execute(select);
        List<Auction> auctions = new ArrayList<>();

        result.forEach(x -> auctions.add(auctionMapper.map(x)));
        return auctions;
    }

    @PostConstruct
    private void init() {
        INSERT_INTO_AUCTIONS = session.prepare(auctionMapper.getInsertStatement(TABLE_NAME));
    }
}
