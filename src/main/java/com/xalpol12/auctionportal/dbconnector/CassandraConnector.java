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

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CassandraConnector {

    @Value("${CASS_HOST_NAME}")
    private String hostNames;
    @Value("${REPLICATION_STRATEGY}")
    private String replicationStrategy;
    @Value("${REPLICATION_FACTOR}")
    private String replicationFactor;

    private static int CONNECTION_RETRIES = 5;
    private static int RETRY_DELAY_SECONDS = 10;

    @Getter
    private Session session;
    private Cluster cluster;

    @PreDestroy
    public void close() {
        if (session != null) {
            session.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }

    @PostConstruct
    public void init() {
        connect();
        setUpDb();
    }

    private void connect() {
        for (int i = 0; i < CONNECTION_RETRIES; i++) {
            try {
                Builder builder = Cluster.builder();
                Arrays.stream(hostNames.split(","))
                        .forEach(builder::addContactPoint);
                cluster = builder.withPort(9042)
//                .withLoadBalancingPolicy()  // TODO: discuss available configs https://docs.datastax.com/en/developer/java-driver/3.2/manual/load_balancing/index.html
//                .withSpeculativeExecutionPolicy() // TODO: same as above https://docs.datastax.com/en/developer/java-driver/3.2/manual/speculative_execution/index.html
//                .withRetryPolicy() // TODO: same as above https://docs.datastax.com/en/developer/java-driver/3.2/manual/retries/index.html
                        .build();
                session = cluster.connect();
                log.info("Successfully connected to Cassandra cluster: {}", cluster.getMetadata().getClusterName());
                return;
            } catch (Exception e) {
                log.warn("Failed to connect to Cassandra. Retrying in {} seconds... ({}/{})", RETRY_DELAY_SECONDS, i + 1, CONNECTION_RETRIES);
                try {
                    Thread.sleep(RETRY_DELAY_SECONDS * 1000L);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Connection retry interrupted", interruptedException);
                }
            }

        }
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

        session.execute("DROP TABLE IF EXISTS USERS;");
        session.execute("DROP TABLE IF EXISTS AUCTIONS;");
        session.execute("DROP TABLE IF EXISTS BIDS;");
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
                .append("PRIMARY KEY (auction_id, bid_value, id))")
                .append("WITH CLUSTERING ORDER BY (bid_value DESC);");

        String createBids = sb3.toString();
        var bidsResult = session.execute(createBids);

        return usersResult.wasApplied() && auctionsResult.wasApplied() && bidsResult.wasApplied();
    }

    private void runInserts() {
        session.execute("USE AUCTIONPORTAL");

        session.execute("INSERT INTO USERS (id, name) VALUES (6ef8fb66-8572-4bb0-af8e-48549ca2d071, 'Artur');");
        session.execute("INSERT INTO USERS (id, name) VALUES (6ef8fb66-8572-4bb0-af8e-48549ca2d072, 'Fryderyk');");
        session.execute("INSERT INTO USERS (id, name) VALUES (6ef8fb66-8572-4bb0-af8e-48549ca2d073, 'Simona');");
        session.execute("INSERT INTO USERS (id, name) VALUES (6ef8fb66-8572-4bb0-af8e-48549ca2d074, 'Hanna');");

        session.execute("INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) " +
                "VALUES (edc6981b-db87-41ec-a62a-4c1900ed8a9a, '2025-12-31T23:59:59.000+0000', '2024-11-01T12:00:00.000+0000', 'Sprzedam OPLA tanio', 1.00);");

        LocalDateTime datePlusThree = LocalDateTime.now().plusMinutes(3);
        String insertJaja = "INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) VALUES (01649ac3-7f29-40fa-bf35-7535579a272f, '"
                + datePlusThree + "', '2024-11-01T12:00:00.000+0000', 'Jaja wiejskie 12 sztuk', 12.00);";
        session.execute(insertJaja);

        LocalDateTime datePlusFive = LocalDateTime.now().plusMinutes(5);
        String insertIphone = "INSERT INTO AUCTIONS (id, end_date, start_date, auction_name, start_price) VALUES (55f7fe87-68de-40a8-99b8-7246d8608404, '"
                + datePlusFive + "', '2024-11-01T12:00:00.000+0000', 'IPhone20', 5.00);";
        session.execute(insertIphone);
    }
}