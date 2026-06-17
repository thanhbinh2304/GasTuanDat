package com.example.GasTuanDat.purchase.dtos;

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
public class PurchaseDetailResponse {
    private UUID id;
    private UUID purchaseId;
    private UUID productId;
    private String productCode;
    private String productName;
    private String unit;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private BigDecimal total;
}
