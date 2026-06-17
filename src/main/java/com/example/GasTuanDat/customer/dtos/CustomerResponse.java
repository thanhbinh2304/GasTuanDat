package com.example.GasTuanDat.customer.dtos;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private UUID customerId;
    
    private String customerCode;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Boolean gender;

    private LocalDate dateOfBirth;

    private String note;

    private String address;

    private BigDecimal debt;

    private UUID wardId;

    private String wardName;

    private UUID customerGroupId;

    private String customerGroupName;

    private OffsetDateTime lastTransactionDate;

    private UUID areaId;

    private String areaName;
}
