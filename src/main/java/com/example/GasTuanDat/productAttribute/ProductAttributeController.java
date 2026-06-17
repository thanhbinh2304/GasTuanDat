package com.example.GasTuanDat.productAttribute;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import java.util.Map;
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
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeResponse;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/product-attributes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "ProductAttribute", description = "Product attribute management APIs")
public class ProductAttributeController {
    ProductAttributeService productAttributeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create product attribute (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductAttributeResponse> create(
            @Valid @RequestBody ProductAttributeCreateRequest request) {
        return ApiResponse.<ProductAttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Create product attribute success")
                .data(productAttributeService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Search product attributes with pagination")
    public ApiResponse<java.util.List<ProductAttributeResponse>> search(
            @RequestParam(required = false) String attributeValue,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<ProductAttributeResponse> result = productAttributeService.search(attributeValue, page, limit);
        return ApiResponse.<java.util.List<ProductAttributeResponse>>builder()
                .success(true)
                .code(200)
                .message("Search product attributes success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product attribute by id")
    public ApiResponse<ProductAttributeResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<ProductAttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Get product attribute success")
                .data(productAttributeService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update product attribute (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductAttributeResponse> update(@PathVariable UUID id,
            @Valid @RequestBody ProductAttributeUpdateRequest request) {
        return ApiResponse.<ProductAttributeResponse>builder()
                .success(true)
                .code(200)
                .message("Update product attribute success")
                .data(productAttributeService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.example.GasTuanDat.common.constants.RoleConstants).ADMIN)")
    @Operation(summary = "Delete product attribute (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        productAttributeService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete product attribute success")
                .build();
    }
}
