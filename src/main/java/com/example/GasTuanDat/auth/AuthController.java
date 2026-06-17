package com.example.GasTuanDat.auth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.auth.dtos.ApproveForgotPasswordRequest;
import com.example.GasTuanDat.auth.dtos.ApproveForgotPasswordResponse;
import com.example.GasTuanDat.auth.dtos.ChangePasswordCodeResponse;
import com.example.GasTuanDat.auth.dtos.ChangePasswordRequest;
import com.example.GasTuanDat.auth.dtos.ChangePasswordResponse;
import com.example.GasTuanDat.auth.dtos.ForgotPasswordRequest;
import com.example.GasTuanDat.auth.dtos.ForgotPasswordResponse;
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

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login", description = "Authenticate and return access token + refresh token")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login success")
                .data(authService.login(request))
                .build();
    }

    @PostMapping(value = "/forgot-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Forgot password", description = "Create a password reset request and notify owner")
    public ApiResponse<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ApiResponse.<ForgotPasswordResponse>builder()
                .code(200)
                .message("Password reset request created")
                .data(authService.forgotPassword(request))
                .build();
    }

    @PostMapping(value = "/forgot-password/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Approve forgot password", description = "Owner approves request and new password is sent")
    public ApiResponse<ApproveForgotPasswordResponse> approveForgotPassword(
            @Valid @RequestBody ApproveForgotPasswordRequest request) {
        return ApiResponse.<ApproveForgotPasswordResponse>builder()
                .code(200)
                .message("Password reset approved")
                .data(authService.approveForgotPassword(request))
                .build();
    }

    @GetMapping(value = "/forgot-password/approve")
    @Operation(summary = "Approve forgot password by link", description = "Owner approves request from email button")
    public ApiResponse<ApproveForgotPasswordResponse> approveForgotPasswordByLink(
            @RequestParam java.util.UUID requestId,
            @RequestParam java.util.UUID approvalToken) {
        ApproveForgotPasswordRequest request = ApproveForgotPasswordRequest.builder()
                .requestId(requestId)
                .approvalToken(approvalToken)
                .build();

        return ApiResponse.<ApproveForgotPasswordResponse>builder()
                .code(200)
                .message("Password reset approved")
                .data(authService.approveForgotPassword(request))
                .build();
    }



    @PostMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change password", description = "Change password for logged-in Sale/Warehouse employee using verification code")
    public ApiResponse<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ApiResponse.<ChangePasswordResponse>builder()
                .code(200)
                .message("Password changed successfully")
                .data(authService.changePassword(request))
                .build();
    }
}
