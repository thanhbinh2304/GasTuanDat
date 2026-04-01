package com.example.GasTuanDat.role;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleEntity getByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
    }
}
