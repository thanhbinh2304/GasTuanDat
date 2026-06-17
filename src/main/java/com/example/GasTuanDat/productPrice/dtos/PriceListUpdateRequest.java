package com.example.GasTuanDat.productPrice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListUpdateRequest {
    @NotBlank(message = "Name is required")
    private String priceListName;
}
