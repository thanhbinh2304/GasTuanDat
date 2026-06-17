package com.example.GasTuanDat.purchase.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
public class PurchaseOrderCreateRequest {
    private String purchaseCode;

    private OffsetDateTime purchaseDate;

    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;

    private UUID employeeId;
    private UUID supplierId;
    private UUID stockId;

    private String note;
    private String orderType;
}
