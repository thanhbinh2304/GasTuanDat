package com.example.GasTuanDat.gasBook.dtos;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GasBookCreateRequest {
    private String gasBookCode;

    @NotBlank
    private String fullName;

    private String email;

    private String phoneNumber;

    private Boolean gender;

    private LocalDate dateOfBirth;

    private String note;

    private String address;

    private UUID wardId;

    private UUID customerGroupId;

    private Integer points;

    private Integer cycle;
}
