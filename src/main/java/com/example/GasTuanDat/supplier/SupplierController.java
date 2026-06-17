package com.example.GasTuanDat.supplier;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.supplier.dtos.SupplierCreateRequest;
import com.example.GasTuanDat.supplier.dtos.SupplierResponse;
import com.example.GasTuanDat.supplier.dtos.SupplierUpdateRequest;
import com.example.GasTuanDat.supplier.mapper.SupplierMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Supplier", description = "Supplier management APIs")
public class SupplierController {
    SupplierService supplierService;
    SupplierMapper supplierMapper;

    /**
     * Create new supplier
     * Permission: warehouse, admin
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create supplier (WAREHOUSE, ADMIN)")
    public ApiResponse<SupplierResponse> create(@Valid @RequestBody SupplierCreateRequest request) {
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Create supplier success")
                .data(supplierService.create(request))
                .build();
    }

    /**
     * Get supplier by ID
     * Permission: all role
     */
    @GetMapping("/{supplierId}")
    @Operation(summary = "Get supplier by id (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<SupplierResponse> getById(@PathVariable UUID supplierId) {
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Get supplier success")
                .data(supplierService.getById(supplierId))
                .build();
    }

    /**
     * Update supplier information
     * Permission: warehouse, admin
     */
    @PutMapping("/{supplierId}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update supplier (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<SupplierResponse> update(
            @PathVariable UUID supplierId,
            @Valid @RequestBody SupplierUpdateRequest request) {
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Update supplier success")
                .data(supplierService.update(supplierId, request))
                .build();
    }

    /**
     * Delete supplier by ID
     * Permission: admin only
     */
    @DeleteMapping("/{supplierId}")
    @PreAuthorize("hasRole('" + ADMIN + "')")
    @Operation(summary = "Delete supplier (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID supplierId) {
        supplierService.delete(supplierId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete supplier success")
                .build();
    }

    /**
     * Search suppliers with filters and pagination
     * Permission: all role
     * Supports search by keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search suppliers with pagination")
    public ApiResponse<PageResult<SupplierResponse>> searchSupplier(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {

        PageResult<SupplierResponse> result = supplierService.search(keyword, page, limit);

        return ApiResponse.<PageResult<SupplierResponse>>builder()
                .code(200)
                .message("Search suppliers success")
                .data(result)
                .build();
    }
}
