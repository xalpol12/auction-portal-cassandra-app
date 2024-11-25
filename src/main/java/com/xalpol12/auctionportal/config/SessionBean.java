package com.xalpol12.auctionportal.config;

import com.datastax.driver.core.Session;
import com.xalpol12.auctionportal.dbconnector.CassandraConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionBean {

    @Bean
    public Session session(CassandraConnector cassandraConnector) {
        return cassandraConnector.getSession();
    }
}