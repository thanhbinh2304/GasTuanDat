package com.example.GasTuanDat.purchase.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {
    private UUID purchaseId;
    private String purchaseCode;
    private OffsetDateTime purchaseDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;
    private UUID employeeId;
    private String employeeName;
    private UUID supplierId;
    private String supplierCode;
    private String supplierName;
    private UUID stockId;
    private String stockName;
    private String note;
    private String orderType;
}
