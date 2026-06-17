package com.example.GasTuanDat.product.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDto {
    private UUID priceListId;
    private String priceListName;
    private BigDecimal price;
}
