package com.xalpol12.auctionportal.model;

import com.xalpol12.auctionportal.model.enums.BidValidity;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
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

    public record BidInput(
            @NonNull
            UUID auctionId,
            @NonNull
            UUID userId,
            @DecimalMin(value = "0.0", inclusive = false)
            BigDecimal bidValue) {
    }
}
