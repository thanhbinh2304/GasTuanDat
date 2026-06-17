package com.example.GasTuanDat.stockTake.dtos;

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
public class StockTakeCreateRequest {
    private String stockTakeCode;

    @NotNull(message = "stockTakeDate is required")
    private OffsetDateTime stockTakeDate;

    private String note;

    private UUID employeeId;
    private UUID stockId;

    private java.util.List<StockTakeDetailCreateRequest> details;
}
