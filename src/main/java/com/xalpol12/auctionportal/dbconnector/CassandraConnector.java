package com.xalpol12.auctionportal.dbconnector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CassandraConnector {

    @Value("${REPLICATION_STRATEGY}")
    private String replicationStrategy;
    @Value("${REPLICATION_FACTOR}")
    private String replicationFactor;

    @Getter
    private Session session;
    private Cluster cluster;

    public CassandraConnector() {
        connect();
    }

    public void connect() {
        Builder builder = Cluster.builder().addContactPoint("cass-1").withPort(9042);
        cluster = builder.build();
        session = cluster.connect();
    }

    @PreDestroy
    public void close() {
        session.close();
        cluster.close();
    }

    @PostConstruct
    public void setUpDb() {
        if (initializeKeyspace()) log.info("Keyspace initialised successfully") else ;
    }

    private boolean initializeKeyspace() {
        String query = "CREATE KEYSPACE IF NOT EXISTS " +
                "auctionPortal" + " WITH replication = {" +
                "'class':'" + replicationStrategy +
                "','replication_factor':" + replicationFactor +
                "};";
        var result = session.execute(query);
        return result.wasApplied();
    }

    private boolean initializeTables() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("").append("(")
                .append("subject text);");

        String query = sb.toString();
        var result = session.execute(query);
        return result.wasApplied();
    }
}
