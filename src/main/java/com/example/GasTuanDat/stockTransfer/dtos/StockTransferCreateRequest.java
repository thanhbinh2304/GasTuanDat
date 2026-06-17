package com.example.GasTuanDat.stockTransfer.dtos;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferCreateRequest {
    private String transferCode;

    @NotNull(message = "transferDate is required")
    private OffsetDateTime transferDate;

    private String note;

    private UUID employeeId;
    private UUID fromStockId;
    private UUID toStockId;

    private List<StockTransferDetailCreateRequest> details;
}
