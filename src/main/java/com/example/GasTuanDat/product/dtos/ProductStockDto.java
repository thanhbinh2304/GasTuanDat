package com.example.GasTuanDat.product.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDto {
    private UUID stockId;
    private String name;
    private Integer quantity;
}
