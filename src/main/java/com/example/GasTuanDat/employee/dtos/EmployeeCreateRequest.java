package com.example.GasTuanDat.employee.dtos;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotNull(message = "Employee code is required")
    private String employeeCode;

    private UUID departmentId;

    private String note;

    private Boolean status;

    private LocalDate hireDate;
}