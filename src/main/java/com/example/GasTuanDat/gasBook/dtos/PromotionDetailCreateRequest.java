package com.example.GasTuanDat.gasBook.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailCreateRequest {
    private String productId; // UUID as string
    private Integer quantity;
}
