package com.example.GasTuanDat.account;

import com.example.GasTuanDat.account.dtos.*;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.role.RoleEntity;
import com.example.GasTuanDat.role.RoleService;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AccountEntity create(AccountCreationRequest dto) {
        RoleEntity employeeRole = roleService.getByName("employee");

        AccountEntity acc = AccountEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(employeeRole)
                .build();

        return accountRepository.save(acc);
    }

    public java.util.List<AccountEntity> getAll() {
        return accountRepository.findAll();
    }

    public AccountEntity getById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

}
