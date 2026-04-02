package com.example.GasTuanDat.employee.dtos;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest {
    private UUID accountId;

    private String employeeCode;

    private UUID departmentId;

    private String note;

    private Boolean status;

    private LocalDate hireDate;
}