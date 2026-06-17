package com.example.GasTuanDat.purchase.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetailCreateRequest {
    @NotNull
    private UUID purchaseId;

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal purchasePrice;

    private BigDecimal total;
}
