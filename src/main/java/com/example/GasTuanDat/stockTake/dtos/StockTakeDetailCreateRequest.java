package com.example.GasTuanDat.stockTake.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTakeDetailCreateRequest {
    @NotNull(message = "stockTakeId is required")
    private UUID stockTakeId;

    @NotNull(message = "productId is required")
    private UUID productId;

    private Short systemQuantity;
    private Short actualQuantity;
}
