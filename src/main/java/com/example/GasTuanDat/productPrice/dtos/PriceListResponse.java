package com.example.GasTuanDat.productPrice.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListResponse {
    private UUID priceListId;
    private String priceListName;
}
