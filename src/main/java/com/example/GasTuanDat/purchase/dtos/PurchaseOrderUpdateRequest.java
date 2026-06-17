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
public class PurchaseOrderUpdateRequest {
    private String purchaseCode;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;

    private UUID employeeId;
    private UUID supplierId;
    private UUID stockId;

    private String note;
    private String orderType;
}
