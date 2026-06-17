package com.example.GasTuanDat.stockTake.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTakeDetailUpdateRequest {
    private java.util.UUID productId;
    private Short systemQuantity;
    private Short actualQuantity;
}
