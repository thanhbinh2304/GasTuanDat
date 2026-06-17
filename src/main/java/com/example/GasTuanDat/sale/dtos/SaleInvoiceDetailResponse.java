package com.example.GasTuanDat.sale.dtos;

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
public class SaleInvoiceDetailResponse {
    private UUID id;
    private UUID invoiceId;
    private UUID productId;
    private String productCode;
    private String productName;
    private Short unit;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}
