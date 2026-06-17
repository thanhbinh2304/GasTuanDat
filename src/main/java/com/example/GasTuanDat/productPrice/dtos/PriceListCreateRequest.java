package com.example.GasTuanDat.productPrice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PriceListCreateRequest {
    @NotBlank(message = "Price list name is required")
    private String priceListName;
}
