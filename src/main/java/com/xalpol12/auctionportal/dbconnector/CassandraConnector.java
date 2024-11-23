package com.xalpol12.auctionportal.dbconnector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CassandraConnector {

    @Value("${CASS_HOST_NAME}")
    private String hostName;
    @Value("${REPLICATION_STRATEGY}")
    private String replicationStrategy;
    @Value("${REPLICATION_FACTOR}")
    private String replicationFactor;

    @Getter
    private Session session;
    private Cluster cluster;

    @PreDestroy
    public void close() {
        session.close();
        cluster.close();
    }

    @PostConstruct
    public void init() {
        connect();
        setUpDb();
    }

    private void connect() {
        Builder builder = Cluster.builder().addContactPoint(hostName).withPort(9042);
        cluster = builder.build();
        session = cluster.connect();
    }

    private void setUpDb() {
        if (initializeKeyspace()) {
            log.info("Keyspace initialised successfully");
        } else {
            log.info("Keyspace not initialised");
        }

        if (initializeTables()) {
            log.info("Tables initialised successfully");
        } else {
            log.info("Tables not initialised");
        }
    }

    private boolean initializeKeyspace() {
        String query = "CREATE KEYSPACE IF NOT EXISTS " +
                "auctionPortal" + " WITH replication = {" +
                "'class':'" + replicationStrategy +
                "','replication_factor':" + replicationFactor +
                "};";
        var result = session.execute(query);
        session.execute("use auctionPortal");
        return result.wasApplied();
    }

    private boolean initializeTables() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("USERS").append("(")
                .append("id UUID, ")
                .append("name UUID, ")
                .append("auctions LIST<UUID>, ")
                .append("PRIMARY KEY (name));");

        String createUsers = sb.toString();
        var usersResult = session.execute(createUsers);

        StringBuilder sb2 = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("AUCTIONS").append("(")
                .append("id UUID, ")
                .append("start_date TIMESTAMP, ")
                .append("end_date TIMESTAMP, ")
                .append("auction_name text, ")
                .append("start_price DECIMAL, ")
                .append("status text, ")
                .append("auction_winner UUID, ")
                .append("PRIMARY KEY (status, id, end_date))")
                .append("WITH CLUSTERING ORDER BY (id ASC, end_date ASC);");
        String createAuctions = sb2.toString();
        var auctionsResult = session.execute(createAuctions);

        // TODO: Ponder idea of creating redundancy in tables to get easier access to data

        StringBuilder sb3 = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("BIDS").append("(")
                .append("auction_id UUID, ")
                .append("id UUID, ")
                .append("user_id UUID, ")
                .append("bid_value DECIMAL, ")
                .append("bid_timestamp TIMESTAMP, ")
                .append("bid_validity TEXT, ")
                .append("PRIMARY KEY (auction_id, bid_value))")
                .append("WITH CLUSTERING ORDER BY (bid_value DESC);");

        String createBids = sb3.toString();
        var bidsResult = session.execute(createBids);

        return usersResult.wasApplied() && auctionsResult.wasApplied() && bidsResult.wasApplied();
    }
}
