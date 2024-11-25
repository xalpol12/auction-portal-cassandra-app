package com.xalpol12.auctionportal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@OpenAPIDefinition
public class AuctionPlatform {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AuctionPlatform.class, args);
	}
}