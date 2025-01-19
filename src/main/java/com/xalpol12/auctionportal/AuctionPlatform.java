package com.xalpol12.auctionportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {CassandraAutoConfiguration.class})
public class AuctionPlatform {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AuctionPlatform.class, args);
    }
}