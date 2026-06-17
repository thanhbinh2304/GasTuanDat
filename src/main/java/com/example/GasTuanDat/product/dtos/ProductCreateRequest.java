package com.example.GasTuanDat.product.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    private String productCode;

    @NotBlank(message = "productName is required")
    private String productName;

    @NotNull(message = "cost is required")
    @PositiveOrZero(message = "cost must be greater than or equal to 0")
    private BigDecimal cost;

    @NotNull(message = "categoryId is required")
    private UUID categoryId;

    private String unit;

    private String note;

    private List<ProductStockDto> warehouses;
    private List<ProductPriceDto> priceTiers;
    private List<ProductAttributeDto> attributesList;
}
