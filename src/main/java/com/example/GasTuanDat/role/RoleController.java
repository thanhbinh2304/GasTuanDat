package com.example.GasTuanDat.role;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.role.dtos.RoleCreationRequest;
import com.example.GasTuanDat.role.dtos.RoleUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role", description = "Role management APIs")
public class RoleController {
    RoleService roleService;

    @PostMapping
    @Operation(summary = "Create role")
    public ApiResponse<RoleEntity> create(@Valid @RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleEntity>builder()
                .code(200)
                .message("Create role success")
                .data(roleService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    public ApiResponse<List<RoleEntity>> getAll() {
        return ApiResponse.<List<RoleEntity>>builder()
                .code(200)
                .message("Get roles success")
                .data(roleService.getAll())
                .build();
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "Get role by id")
    public ApiResponse<RoleEntity> getById(@PathVariable UUID roleId) {
        return ApiResponse.<RoleEntity>builder()
                .code(200)
                .message("Get role success")
                .data(roleService.getById(roleId))
                .build();
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "Update role")
    public ApiResponse<RoleEntity> update(@PathVariable UUID roleId, @Valid @RequestBody RoleUpdateRequest request) {
        return ApiResponse.<RoleEntity>builder()
                .code(200)
                .message("Update role success")
                .data(roleService.update(roleId, request))
                .build();
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "Delete role")
    public ApiResponse<Void> delete(@PathVariable UUID roleId) {
        roleService.delete(roleId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete role success")
                .build();
    }
}
