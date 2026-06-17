package com.example.GasTuanDat.productAttribute.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductAttributeUpdateRequest {
    @NotNull(message = "Attribute ID is required")
    private UUID attributeId;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotBlank(message = "Attribute value is required")
    private String attributeValue;
}
