package com.example.GasTuanDat.stockTransfer.dtos;

import java.time.OffsetDateTime;
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
public class StockTransferResponse {
    private UUID transferId;
    private String transferCode;
    private OffsetDateTime transferDate;
    private String note;

    private UUID employeeId;
    private String employeeName;
    private UUID fromStockId;
    private String fromStockName;
    private UUID toStockId;
    private String toStockName;

    private List<StockTransferDetailResponse> details;
}
