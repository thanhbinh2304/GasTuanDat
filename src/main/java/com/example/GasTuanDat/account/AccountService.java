package com.example.GasTuanDat.account;

import com.example.GasTuanDat.account.dtos.*;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.department.DepartmentEntity;
import com.example.GasTuanDat.department.DepartmentRepository;
import com.example.GasTuanDat.employee.EmployeeEntity;
import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.profile.ProfileEntity;
import com.example.GasTuanDat.profile.ProfileRepository;
import com.example.GasTuanDat.role.RoleEntity;
import com.example.GasTuanDat.role.RoleService;

import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public AccountEntity create(AccountCreationRequest dto) {
        RoleEntity employeeRole = roleService.getByName("employee");
        DepartmentEntity department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ApiException(ErrorCode.DEPARTMENT_NOT_FOUND));
        String prefix = normalizeDepartmentPrefix(department.getName());

        AccountEntity acc = AccountEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(employeeRole)
                .build();

        AccountEntity savedAccount = accountRepository.save(acc);

        long nextNumber = employeeRepository.countByEmployeeCodeStartingWith(prefix) + 1;
        String employeeCode = prefix + String.format("%03d", nextNumber);

        EmployeeEntity employee = EmployeeEntity.builder()
                .accountId(savedAccount.getAccountId())
                .employeeCode(employeeCode)
                .departmentId(dto.getDepartmentId())
                .status(true)
                .build();
        employeeRepository.save(employee);

        ProfileEntity profile = ProfileEntity.builder()
                .accountId(savedAccount.getAccountId())
                .build();
        profileRepository.save(profile);

        return savedAccount;
    }

    public java.util.List<AccountEntity> getAll() {
        return accountRepository.findAll();
    }

    public AccountEntity getById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private String normalizeDepartmentPrefix(String departmentName) {
        String normalized = departmentName == null ? "" : departmentName.trim().toUpperCase(Locale.ROOT);
        if ("SALE".equals(normalized) || "KHO".equals(normalized)) {
            return normalized;
        }
        throw new ApiException(ErrorCode.INVALID_INPUT);
    }

}
