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
    private String employeeCode;

    private UUID positionId;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String note;

    private Boolean status;

    private LocalDate hireDate;
    
    private UUID wardId;
}