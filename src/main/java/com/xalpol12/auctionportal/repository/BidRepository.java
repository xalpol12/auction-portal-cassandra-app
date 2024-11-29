package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.xalpol12.auctionportal.model.Bid;
import com.xalpol12.auctionportal.model.enums.BidValidity;
import com.xalpol12.auctionportal.repository.mappers.CassandraMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BidRepository {
    private static final String TABLE_NAME = "BIDS";
    private final Session session;
    private final CassandraMapper<Bid, Bid.BidInput> bidMapper;

    private PreparedStatement SELECT_ALL_WITH_AUCTION_ID;
    private PreparedStatement INSERT_INTO_BIDS;
    private PreparedStatement UPDATE_BID_VALIDITY;

    public List<Bid> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<Bid> bids = new ArrayList<>();

        result.forEach(x -> bids.add(bidMapper.map(x)));

        return bids;
    }

    public Bid insert(Bid.BidInput bidInput) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_BIDS);
        Bid bid = bidMapper.map(bidInput);

        bsInsert.bind(
                bid.getAuctionId(),
                bid.getId(),
                bid.getUserId(),
                bid.getBidValue(),
                bid.getBidTime(),
                bid.getBidValidity().toString()
        );

        ResultSet insertResult = session.execute(bsInsert);

        if (insertResult.wasApplied()) {
            return bid;
        } else {
            throw new RuntimeException("Insert failed");
        }
    }

    public List<Bid> selectAllByAuctionId(UUID auctionId) {
        BoundStatement bsSelect = new BoundStatement(SELECT_ALL_WITH_AUCTION_ID);
        bsSelect.bind(auctionId);
        ResultSet result = session.execute(bsSelect);
        List<Bid> bids = new ArrayList<>();

        result.forEach(x -> bids.add(bidMapper.map(x)));

        return bids;
    }

    public Bid update(Bid bid) {
        BoundStatement bsUpdate = new BoundStatement(UPDATE_BID_VALIDITY);
        bid.setBidValidity(BidValidity.VALID);
        bsUpdate.bind(
                bid.getBidValidity().toString(),
                bid.getAuctionId(),
                bid.getBidValue(),
                bid.getId());

        ResultSet updateResult = session.execute(bsUpdate);

        if (updateResult.wasApplied()) {
            return bid;
        } else {
            throw new RuntimeException("Update failed");
        }
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

        // TODO: Fix
        UPDATE_BID_VALIDITY = session.prepare(
                new StringBuilder("UPDATE ")
                        .append(TABLE_NAME)
                        .append(" SET bid_validity=?")
                        .append(" WHERE ")
                        .append("auction_id=? AND bid_value=? AND id=?")
                        .toString()
        );
    }

}
