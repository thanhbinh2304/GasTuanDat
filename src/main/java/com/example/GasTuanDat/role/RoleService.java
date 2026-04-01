package com.example.GasTuanDat.role;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.role.dtos.RoleCreationRequest;
import com.example.GasTuanDat.role.dtos.RoleUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleEntity create(RoleCreationRequest request) {
        RoleEntity role = RoleEntity.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();

        return roleRepository.save(role);
    }

    public List<RoleEntity> getAll() {
        return roleRepository.findAll();
    }

    public RoleEntity getById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    }

    public RoleEntity getByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    }

    public RoleEntity update(UUID roleId, RoleUpdateRequest request) {
        RoleEntity role = getById(roleId);
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        return roleRepository.save(role);
    }

    public void delete(UUID roleId) {
        RoleEntity role = getById(roleId);
        roleRepository.delete(role);
    }
}
