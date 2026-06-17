package com.example.GasTuanDat.account.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequest {
    // Password might be empty if the user doesn't want to change it
    private String password;

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Status is required")
    private Boolean status;
}
