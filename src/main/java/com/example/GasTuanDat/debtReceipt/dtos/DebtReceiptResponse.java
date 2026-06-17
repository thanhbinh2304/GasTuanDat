package com.example.GasTuanDat.debtReceipt.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtReceiptResponse {
    private UUID id;
    private String code;
    private String customerCode;
    private String customerName;
    private LocalDate debtDate;
    private LocalDate dueDate;
    private String status;
    private String notes;
    private List<DebtReceiptDetailResponse> items;
}
