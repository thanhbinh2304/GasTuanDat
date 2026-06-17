package com.example.GasTuanDat.productAttribute.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttributeCreateRequest {
    @NotBlank(message = "Attribute name is required")
    private String attributeName;
}
