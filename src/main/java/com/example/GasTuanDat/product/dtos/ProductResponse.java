package com.example.GasTuanDat.product.dtos;

import java.math.BigDecimal;
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
public class ProductResponse {
    private UUID productId;
    private String productCode;
    private String productName;
    private BigDecimal cost;
    private UUID categoryId;
    private String categoryName;
    private String unit;
    private String note;

    private List<ProductStockDto> warehouses;
    private List<ProductPriceDto> priceTiers;
    private List<ProductAttributeDto> attributesList;
}
