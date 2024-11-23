package com.xalpol12.auctionportal;

import com.xalpol12.auctionportal.dbconnector.CassandraConnector;
import com.xalpol12.auctionportal.repository.CassandraRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AuctionPlatform {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AuctionPlatform.class, args);
	}
}