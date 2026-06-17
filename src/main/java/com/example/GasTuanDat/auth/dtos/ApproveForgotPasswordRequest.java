package com.example.GasTuanDat.auth.dtos;

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
public class ApproveForgotPasswordRequest {
    @NotNull(message = "Request id is required")
    private UUID requestId;

    @NotNull(message = "Approval token is required")
    private UUID approvalToken;
}
