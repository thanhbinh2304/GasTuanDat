package com.example.GasTuanDat.supplier.dtos;

import java.sql.Timestamp;
import java.util.UUID;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {
    private UUID supplierId;
    private String fullName;
    private String phoneNumber;
    private String wardId;
    private String email;
    private String note;
    private String address;
    private BigDecimal debt;
}
