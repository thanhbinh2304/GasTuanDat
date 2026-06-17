package com.example.GasTuanDat.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCodeResponse {
    private String status;
    private Integer expiresInMinutes;
}