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

        dropTables();

        if (initializeTables()) {
            log.info("Tables initialised successfully");
        } else {
            log.info("Tables not initialised");
        }

        runInserts();
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

    private void dropTables() {
        session.execute("USE AUCTIONPORTAL");

        session.execute("DROP TABLE IF EXISTS USERS");
        session.execute("DROP TABLE IF EXISTS AUCTIONS");
        session.execute("DROP TABLE IF EXISTS BIDS");
    }

    private boolean initializeTables() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("USERS").append("(")
                .append("id UUID, ")
                .append("name text, ")
                .append("auctions Set<UUID>, ")
                .append("PRIMARY KEY (id));");

        String createUsers = sb.toString();
        var usersResult = session.execute(createUsers);

        StringBuilder sb2 = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("AUCTIONS").append("(")
                .append("id UUID, ")
                .append("start_date TIMESTAMP, ")
                .append("end_date TIMESTAMP, ")
                .append("auction_name text, ")
                .append("start_price DECIMAL, ")
                .append("auction_winner UUID, ")
                .append("PRIMARY KEY (id, end_date, start_date));");
        String createAuctions = sb2.toString();
        var auctionsResult = session.execute(createAuctions);

        StringBuilder sb3 = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("BIDS").append("(")
                .append("auction_id UUID, ")
                .append("id UUID, ")
                .append("user_id UUID, ")
                .append("bid_value DECIMAL, ")
                .append("bid_timestamp TIMESTAMP, ")
                .append("bid_validity TEXT, ")
                .append("PRIMARY KEY (auction_id, bid_validity, bid_value, id))")
                .append("WITH CLUSTERING ORDER BY (bid_validity ASC, bid_value DESC);");

        String createBids = sb3.toString();
        var bidsResult = session.execute(createBids);

        return usersResult.wasApplied() && auctionsResult.wasApplied() && bidsResult.wasApplied();
    }

    private void runInserts() {
        session.execute("USE AUCTIONPORTAL");

        // Insert users
        session.execute("INSERT INTO USERS (id, name) VALUES (uuid(), 'Artur')");
        session.execute("INSERT INTO USERS (id, name) VALUES (uuid(), 'Fryderyk')");
        session.execute("INSERT INTO USERS (id, name) VALUES (uuid(), 'Simona')");
        session.execute("INSERT INTO USERS (id, name) VALUES (uuid(), 'Hanna')");

        session.execute("INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) " +
                "VALUES (uuid(), '2025-12-31T23:59:59.000+0000', '2024-12-01T12:00:00.000+0000', 'Sprzedam OPLA tanio', 1.00)");
        session.execute("INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) " +
                "VALUES (uuid(), '2025-12-31T23:59:59.000+0000', '2024-12-01T12:00:00.000+0000', 'IPhone20', 5.00)");
        session.execute("INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) " +
                "VALUES (uuid(), '2025-12-31T23:59:59.000+0000', '2024-12-01T12:00:00.000+0000', 'Jaja wiejskie 12 sztuk', 12.00)");
    }
}