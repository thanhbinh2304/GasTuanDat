package com.example.GasTuanDat.auth.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveForgotPasswordResponse {
    private UUID requestId;
    private String status;
}
