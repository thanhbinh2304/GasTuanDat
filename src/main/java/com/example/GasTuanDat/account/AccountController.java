package com.example.GasTuanDat.account;

import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.account.dtos.AccountUpdateRequest;
import com.example.GasTuanDat.account.entity.AccountEntity;
import com.example.GasTuanDat.account.dtos.AccountResponse;
import com.example.GasTuanDat.account.mapper.AccountMapper;
import com.example.GasTuanDat.common.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account", description = "Account management APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;
    AccountMapper accountMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create account", description = "Admin creates account by selecting role and existing employee")
    public ApiResponse<AccountResponse> create(@Valid @RequestBody AccountCreationRequest request) {
        AccountEntity created = accountService.create(request);
        AccountResponse resp = accountMapper.toAccountResponse(created);
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .message("Create account success")
                .data(resp)
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
    public ApiResponse<List<AccountResponse>> getAll() {
        List<AccountResponse> list = accountService.getAll().stream()
                .map(accountMapper::toAccountResponse).toList();
        return ApiResponse.<List<AccountResponse>>builder()
                .code(200)
                .message("Get accounts success")
                .data(list)
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update account")
    public ApiResponse<AccountResponse> update(@PathVariable UUID id, @Valid @RequestBody AccountUpdateRequest request) {
        AccountEntity updated = accountService.update(id, request);
        AccountResponse resp = accountMapper.toAccountResponse(updated);
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .message("Update account success")
                .data(resp)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        accountService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete account success")
                .build();
    }

}
