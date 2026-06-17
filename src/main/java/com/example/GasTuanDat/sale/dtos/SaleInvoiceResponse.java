package com.example.GasTuanDat.sale.dtos;

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
public class SaleInvoiceResponse {
    private UUID invoiceId;
    private String invoiceCode;
    private OffsetDateTime invoiceDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;
    private String orderType;
    private String note;
    private String paymentMethod;

    private UUID employeeId;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private UUID gasBookId;
    private UUID stockId;

    private java.util.List<SaleInvoiceDetailResponse> details;
}
