package com.example.GasTuanDat.productAttribute.dtos;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAttributeResponse {
    private UUID id;
    private UUID attributeId;
    private UUID productId;
    private String attributeValue;
}
