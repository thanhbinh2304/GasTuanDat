package com.example.GasTuanDat.cashReceipt.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashReceiptCreateRequest {
    @NotBlank(message = "receiptCode is required")
    private String receiptCode;

    @NotNull(message = "receiptDate is required")
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
