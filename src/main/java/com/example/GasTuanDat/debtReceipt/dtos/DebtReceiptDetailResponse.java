package com.example.GasTuanDat.debtReceipt.dtos;

import java.util.UUID;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtReceiptDetailResponse {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String priceList;
}
