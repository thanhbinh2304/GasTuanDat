package com.example.GasTuanDat.productAttribute;

import java.util.Map;
import java.util.UUID;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.productAttribute.dtos.AttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.AttributeResponse;
import com.example.GasTuanDat.productAttribute.dtos.AttributeUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Attribute", description = "Attribute management APIs")
public class AttributeController {
    AttributeService attributeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create attribute (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<AttributeResponse> create(@Valid @RequestBody AttributeCreateRequest request) {
        return ApiResponse.<AttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Create attribute success")
                .data(attributeService.create(request))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search attributes with pagination")
    public ApiResponse<java.util.List<AttributeResponse>> searchAttribute(
            @RequestParam(required = false) String attributeName,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer limit) {
        PageResult<AttributeResponse> result = attributeService.search(attributeName, page, limit);
        return ApiResponse.<java.util.List<AttributeResponse>>builder()
                .success(true)
                .code(200)
                .message("Search attributes success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/{attributeId}")
    @Operation(summary = "Get attribute by id")
    public ApiResponse<AttributeResponse> getById(@PathVariable UUID attributeId) {
        return ApiResponse.<AttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Get attribute success")
                .data(attributeService.getById(attributeId))
                .build();
    }

    @PutMapping("/{attributeId}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update attribute (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<AttributeResponse> update(@PathVariable UUID attributeId,
            @Valid @RequestBody AttributeUpdateRequest request) {
        return ApiResponse.<AttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Update attribute success")
                .data(attributeService.update(attributeId, request))
                .build();
    }

    @DeleteMapping("/{attributeId}")
    @PreAuthorize("hasRole(T(com.example.GasTuanDat.common.constants.RoleConstants).ADMIN)")
    @Operation(summary = "Delete attribute (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID attributeId) {
        attributeService.delete(attributeId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete attribute success")
                .build();
    }
}
