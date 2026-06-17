package com.example.GasTuanDat.employee.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private UUID employeeId;
    
    @JsonProperty("code")
    private String employeeCode;
    
    @JsonProperty("employeeName")
    private String fullName;
    
    private UUID positionId;
    
    private String positionName;
    
    private String phoneNumber;
    
    private String email;
    
    @JsonProperty("notes")
    private String note;
    
    private Boolean status;
    
    private LocalDate hireDate;
    
    private UUID wardId;
    
    private String wardName;
    
    private UUID areaId;
    
    private String areaName;
}