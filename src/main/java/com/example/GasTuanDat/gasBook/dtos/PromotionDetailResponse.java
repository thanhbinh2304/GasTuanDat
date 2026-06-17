package com.example.GasTuanDat.gasBook.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetailResponse {
    private UUID id;
    private String productId;
    private String productName;
    private Integer quantity;
}
