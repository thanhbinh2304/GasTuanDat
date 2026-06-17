package com.example.GasTuanDat.productAttribute.dtos;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttributeResponse {
    private UUID attributeId;
    private String attributeName;
}
