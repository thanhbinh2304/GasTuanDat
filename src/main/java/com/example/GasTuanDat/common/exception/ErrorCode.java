package com.example.GasTuanDat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found"),
    ROLE_NOT_FOUND(404, "Role not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    INVALID_INPUT(400, "Invalid input"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Don't have permission"),
    INTERNAL_ERROR(500, "Internal server error");

    private final int status;
    private final String message;

}
