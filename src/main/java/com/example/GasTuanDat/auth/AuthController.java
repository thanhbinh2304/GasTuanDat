package com.example.GasTuanDat.auth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.account.AccountEntity;
import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.auth.dtos.LoginRequest;
import com.example.GasTuanDat.auth.dtos.LoginResponse;
import com.example.GasTuanDat.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register account", description = "Create new account with default EMPLOYEE role")
    public ApiResponse<AccountEntity> register(@Valid @RequestBody AccountCreationRequest request) {
        return ApiResponse.<AccountEntity>builder()
                .code(200)
                .message("Register success")
                .data(authService.register(request))
                .build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login", description = "Authenticate and return access token + refresh token")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login success")
                .data(authService.login(request))
                .build();
    }
}
