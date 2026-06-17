package com.example.GasTuanDat.productPrice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private UUID priceListId;
    private String priceListName;
    private BigDecimal sellingPrice;
}
