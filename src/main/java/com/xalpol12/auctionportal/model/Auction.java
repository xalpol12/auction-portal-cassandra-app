package com.xalpol12.auctionportal.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    public record AuctionInput(
            LocalDateTime startDate,
            LocalDateTime endDate,
            @NotBlank
            String auctionName,
            @DecimalMin(value = "0.0", inclusive = false)
            BigDecimal startPrice) {
    }
}
