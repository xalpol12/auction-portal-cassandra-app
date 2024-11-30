package com.xalpol12.auctionportal.model;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bid {
    private UUID auctionId;
    private UUID id;
    private UUID userId;
    private BigDecimal bidValue;
    private LocalDateTime bidTime;

    public record BidInput(
            @NonNull
            UUID auctionId,
            @NonNull
            UUID userId,
            @DecimalMin(value = "0.0", inclusive = false)
            BigDecimal bidValue) {
    }
}
