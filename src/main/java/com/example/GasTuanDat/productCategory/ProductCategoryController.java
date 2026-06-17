package com.example.GasTuanDat.productCategory;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
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
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryCreateRequest;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryResponse;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryUpdateRequest;
import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;
import com.example.GasTuanDat.productCategory.mapper.ProductCategoryMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/productCategory")
@Tag(name = "ProductCategory", description = "Product category management APIs")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCategoryController {
    ProductCategoryService productCategoryService;
    ProductCategoryMapper productCategoryMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create product category (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductCategoryResponse> create(@Valid @RequestBody ProductCategoryCreateRequest request) {
        ProductCategoryEntity created = productCategoryService.create(request);
        return ApiResponse.<ProductCategoryResponse>builder().code(200).message("Create product category success")
                .data(productCategoryMapper.toResponse(created)).build();
    }

    @GetMapping
    public ApiResponse<List<ProductCategoryResponse>> getAll() {
        List<ProductCategoryResponse> data = productCategoryService.getAll().stream().map(productCategoryMapper::toResponse)
                .toList();
        return ApiResponse.<List<ProductCategoryResponse>>builder().code(200).message("Get product categories success")
                .data(data).build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductCategoryResponse>> search(
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductCategoryEntity> result = productCategoryService.search(categoryName, page, size);
        List<ProductCategoryResponse> data = result.getContent().stream().map(productCategoryMapper::toResponse).toList();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getNumber());
        pagination.put("size", result.getSize());
        pagination.put("totalElements", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<List<ProductCategoryResponse>>builder().code(200).message("Search product categories success")
                .data(data).pagination(pagination).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductCategoryResponse> getById(@PathVariable UUID id) {
        ProductCategoryEntity entity = productCategoryService.getById(id);
        return ApiResponse.<ProductCategoryResponse>builder().code(200).message("Get product category success")
                .data(productCategoryMapper.toResponse(entity)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update product category (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductCategoryResponse> update(@PathVariable UUID id,
            @Valid @RequestBody ProductCategoryUpdateRequest request) {
        ProductCategoryEntity updated = productCategoryService.update(id, request);
        return ApiResponse.<ProductCategoryResponse>builder().code(200).message("Update product category success")
                .data(productCategoryMapper.toResponse(updated)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete product category (only ADMIN)")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        productCategoryService.delete(id);
        return ApiResponse.<Void>builder().code(200).message("Delete product category success").build();
    }
}
