package com.example.GasTuanDat.customer.dtos;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateRequest {
    private String customerCode;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Boolean gender;

    private LocalDate dateOfBirth;

    private String note;

    private String address;

    private UUID wardId;

    private UUID customerGroupId;
}
