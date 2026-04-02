package com.example.GasTuanDat.account;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account", description = "Account management APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create account", description = "Create account and auto-create employee/profile records")
    public ApiResponse<AccountEntity> create(@Valid @RequestBody AccountCreationRequest request) {
        return ApiResponse.<AccountEntity>builder()
                .code(200)
                .message("Create account success")
                .data(accountService.create(request))
                .build();
    }

}
