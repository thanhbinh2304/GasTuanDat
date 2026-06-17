package com.example.GasTuanDat.supplier.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCreateRequest {
    @NotBlank(message = "Supplier name is required")
    private String fullName;

    @Pattern(regexp = "^$|^0\\d{9,10}$|^\\+84\\d{9,10}$", message = "Phone number is invalid")
    private String phoneNumber;

    private String wardId;
    private String email;
    private String address;
}
