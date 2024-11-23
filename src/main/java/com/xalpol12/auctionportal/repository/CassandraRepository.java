package com.xalpol12.auctionportal.repository;

import com.xalpol12.auctionportal.dbconnector.CassandraConnector;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CassandraRepository {

    private final CassandraConnector cassandraConnector;

    private final Session session;

    public CassandraRepository(CassandraConnector connector) {
        this.cassandraConnector = connector;
        this.session = connector.getSession();
    }
}
