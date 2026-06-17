package com.example.GasTuanDat.productPrice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceUpdateRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Price list ID is required")
    private UUID priceListId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0000001", message = "Price must be greater than 0")
    private BigDecimal sellingPrice;
}
