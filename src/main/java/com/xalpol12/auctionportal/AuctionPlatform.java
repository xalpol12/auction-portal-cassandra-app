package com.xalpol12.auctionportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuctionPlatform {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AuctionPlatform.class, args);
	}
}