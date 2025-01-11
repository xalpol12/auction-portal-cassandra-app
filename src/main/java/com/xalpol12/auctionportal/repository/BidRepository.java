package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.xalpol12.auctionportal.exception.BidTooLowException;
import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.repository.mappers.CassandraMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BidRepository {
    private static final String TABLE_NAME = "BIDS";
    private final Session session;
    private final CassandraMapper<Bid, Bid.BidInput> bidMapper;

    private PreparedStatement SELECT_ALL_WITH_AUCTION_ID;
    private PreparedStatement INSERT_INTO_BIDS;
    private PreparedStatement SELECT_HIGHEST_BY_AUCTION_ID;

    public List<Bid> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<Bid> bids = new ArrayList<>();

        result.forEach(x -> bids.add(bidMapper.map(x)));

        return bids;
    }

    public Bid insert(Bid bid) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_BIDS);

        bsInsert.bind(
                bid.getAuctionId(),
                bid.getId(),
                bid.getUserId(),
                bid.getBidValue(),
                Date.from(bid.getBidTime().toInstant(ZoneOffset.UTC))
        );


        Bid highestBid = selectHighestByAuctionId(bid.getAuctionId());
        if (Objects.isNull(highestBid) || (highestBid.getBidValue().compareTo(bid.getBidValue()) != 1)) {
            ResultSet insertResult = session.execute(bsInsert);
            if (insertResult.wasApplied()) {
                return bid;
            } else {
                throw new RuntimeException("Insert failed");
            }
        }
        throw new BidTooLowException("Bid too low!");
    }

    public List<Bid> selectAllByAuctionId(UUID auctionId) {
        BoundStatement bsSelect = new BoundStatement(SELECT_ALL_WITH_AUCTION_ID);
        bsSelect.bind(auctionId);
        ResultSet result = session.execute(bsSelect);
        List<Bid> bids = new ArrayList<>();

        result.forEach(x -> bids.add(bidMapper.map(x)));

        return bids;
    }

    public Bid selectHighestByAuctionId(UUID auctionId) {
        BoundStatement bsSelect = new BoundStatement(SELECT_HIGHEST_BY_AUCTION_ID);
        bsSelect.bind(auctionId);
        ResultSet result = session.execute(bsSelect);
        Row row = result.one();
        if (row == null ) {
            return null;
        }
        return bidMapper.map(row);
    }

    @PostConstruct
    private void init() {
        SELECT_ALL_WITH_AUCTION_ID = session.prepare(
                new StringBuilder("SELECT * FROM ")
                        .append(TABLE_NAME)
                        .append(" WHERE auction_id=?;")
                        .toString()
        );

        INSERT_INTO_BIDS = session.prepare(bidMapper.getInsertStatement(TABLE_NAME));
        SELECT_HIGHEST_BY_AUCTION_ID = session.prepare(
                new StringBuilder("SELECT * FROM ")
                        .append(TABLE_NAME)
                        .append(" WHERE auction_id=?")
                        .append(" LIMIT 1;")
                        .toString()
        );
    }

}
