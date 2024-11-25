package com.xalpol12.auctionportal.model;

import com.xalpol12.auctionportal.model.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
    private UUID id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String auctionName;
    private BigDecimal startPrice;
    private AuctionStatus status;
    private UUID auctionWinner;
}
