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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
    private final CassandraMapper<Bid> bidMapper;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

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

    public Bid insert(Bid bid) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_BIDS);
        bsInsert.bind(
                bid.getAuctionId(),
                UUID.randomUUID(),
                bid.getUserId(),
                bid.getBidValue(),
                new Date(),
                BidValidity.INVALID.toString()
        );

        ResultSet insertResult = session.execute(bsInsert);
        Bid insertedBid = bidMapper.map(insertResult.one());

        BoundStatement bsUpdate = new BoundStatement(UPDATE_BID_VALIDITY);
        bsUpdate.bind(BidValidity.VALID.toString(), insertedBid.getAuctionId(), insertedBid.getBidValue(), insertedBid.getId());

        ResultSet updateResult = session.execute(bsUpdate);

        return bidMapper.map(updateResult.one());
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
