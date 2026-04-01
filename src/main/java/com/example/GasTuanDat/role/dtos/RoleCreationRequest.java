package com.example.GasTuanDat.role.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationRequest {
    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;
}