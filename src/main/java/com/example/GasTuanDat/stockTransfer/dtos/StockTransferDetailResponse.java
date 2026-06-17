package com.example.GasTuanDat.stockTransfer.dtos;

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
public class StockTransferDetailResponse {
    private UUID id;
    private UUID transferId;
    private UUID productId;
    private String productName;
    private Short quantity;
}
