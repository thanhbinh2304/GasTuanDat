package com.example.GasTuanDat.debtReceipt.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtReceiptCreateRequest {
    private String code;
    private String customerCode;
    private LocalDate debtDate;
    private LocalDate dueDate;
    private String status;
    private String notes;
    private List<DebtReceiptDetailCreateRequest> items;
}
