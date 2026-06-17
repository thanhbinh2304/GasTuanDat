package com.example.GasTuanDat.payment.dtos;

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
public class PaymentCreateRequest {
    @NotBlank(message = "paymentCode is required")
    private String paymentCode;

    @NotNull(message = "paymentDate is required")
    private OffsetDateTime paymentDate;

    private BigDecimal paymentAmount;
    private Object paymentMethod;
    private String notes;

    private UUID createdById;
    private UUID employeeId;
    private UUID customerId;
    private UUID supplierId;
    private UUID objectId;
    private UUID transactionTypeId;
    private UUID purchaseId;
    private UUID stockId;
}
