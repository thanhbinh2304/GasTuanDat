package com.example.GasTuanDat.stockTake.dtos;

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
public class StockTakeResponse {
    private UUID stockTakeId;
    private String stockTakeCode;
    private OffsetDateTime stockTakeDate;
    private String note;

    private UUID employeeId;
    private String employeeName;
    private UUID stockId;
    private String stockName;

    private java.util.List<StockTakeDetailResponse> details;
}
