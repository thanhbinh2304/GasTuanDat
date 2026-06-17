package com.example.GasTuanDat.cashReceipt.dtos;

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
public class CashReceiptUpdateRequest {
    private String receiptCode;
    private OffsetDateTime receiptDate;
    private BigDecimal receiptAmount;
    private Object paymentMethod;
    private String notes;
    private OffsetDateTime createdDate;

    private UUID createdById;
    private UUID employeeId;
    private UUID customerId;
    private UUID supplierId;
    private UUID objectId;
    private UUID transactionTypeId;
    private UUID invoiceId;
}
