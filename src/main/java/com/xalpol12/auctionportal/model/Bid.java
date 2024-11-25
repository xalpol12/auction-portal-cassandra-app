package com.xalpol12.auctionportal.model;

import com.xalpol12.auctionportal.model.enums.BidValidity;
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
public class Bid {
    private UUID auctionId;
    private UUID id;
    private UUID userId;
    private BigDecimal bidValue;
    private Long bidTime;
    private BidValidity bidValidity;
}
