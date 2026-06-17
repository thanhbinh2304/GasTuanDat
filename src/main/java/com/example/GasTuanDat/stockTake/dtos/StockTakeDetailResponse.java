package com.example.GasTuanDat.stockTake.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTakeDetailResponse {
    private UUID id;
    private UUID stockTakeId;
    private UUID productId;
    private String productName;
    private Short systemQuantity;
    private Short actualQuantity;
}
