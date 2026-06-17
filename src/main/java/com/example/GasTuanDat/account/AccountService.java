package com.example.GasTuanDat.account;

import com.example.GasTuanDat.account.dtos.*;
import com.example.GasTuanDat.account.entity.AccountEntity;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.role.entity.RoleEntity;
import com.example.GasTuanDat.role.RoleService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public AccountEntity create(AccountCreationRequest dto) {
        accountRepository.findByUsername(dto.getUsername())
                .ifPresent(account -> {
                    throw new ApiException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
                });

        RoleEntity selectedRole = roleService.getById(dto.getRoleId());
        EmployeeEntity selectedEmployee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (accountRepository.existsByEmployee_EmployeeId(dto.getEmployeeId())) {
            throw new ApiException(ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT);
        }

        AccountEntity acc = AccountEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(selectedRole)
                .employee(selectedEmployee)
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

    @Transactional
    public AccountEntity update(UUID id, AccountUpdateRequest dto) {
        AccountEntity acc = getById(id);

        if (!acc.getEmployee().getEmployeeId().equals(dto.getEmployeeId())) {
            if (accountRepository.existsByEmployee_EmployeeId(dto.getEmployeeId())) {
                throw new ApiException(ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT);
            }
            EmployeeEntity selectedEmployee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
            acc.setEmployee(selectedEmployee);
        }

        if (!acc.getRole().getRoleId().equals(dto.getRoleId())) {
            RoleEntity selectedRole = roleService.getById(dto.getRoleId());
            acc.setRole(selectedRole);
        }

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            acc.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getStatus() != null) {
            acc.setStatus(dto.getStatus());
        }

        return accountRepository.save(acc);
    }

    @Transactional
    public void delete(UUID id) {
        AccountEntity acc = getById(id);
        accountRepository.delete(acc);
    }
}
