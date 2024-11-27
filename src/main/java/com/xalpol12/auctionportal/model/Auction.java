package com.xalpol12.auctionportal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
    private UUID id;
    private Long startDate;
    private Long endDate;
    private String auctionName;
    private BigDecimal startPrice;
}
