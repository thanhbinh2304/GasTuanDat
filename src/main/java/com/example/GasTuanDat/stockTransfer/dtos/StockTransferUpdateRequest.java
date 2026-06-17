package com.example.GasTuanDat.stockTransfer.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferUpdateRequest {
    private String transferCode;
    private OffsetDateTime transferDate;
    private String note;

    private UUID employeeId;
    private UUID fromStockId;
    private UUID toStockId;
    private List<StockTransferDetailCreateRequest> details;
}
