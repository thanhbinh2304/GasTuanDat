package com.example.GasTuanDat.productPrice;

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
import com.example.GasTuanDat.productPrice.dtos.ProductPriceCreateRequest;
import com.example.GasTuanDat.productPrice.dtos.ProductPriceResponse;
import com.example.GasTuanDat.productPrice.dtos.ProductPriceUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/product-price")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "ProductPrice", description = "Product price management APIs")
public class ProductPriceController {
    ProductPriceService productPriceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('NHÂN VIÊN KHO', 'ADMIN')")
    @Operation(summary = "Create product price (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductPriceResponse> create(@Valid @RequestBody ProductPriceCreateRequest request) {
        return ApiResponse.<ProductPriceResponse>builder()
                .success(true)
                .code(200)
                .message("Create product price success")
                .data(productPriceService.create(request))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search product prices with pagination")
    public ApiResponse<java.util.List<ProductPriceResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer limit) {
        PageResult<ProductPriceResponse> result = productPriceService.search(name, page, limit);
        return ApiResponse.<java.util.List<ProductPriceResponse>>builder()
                .success(true)
                .code(200)
                .message("Search product prices success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product price by id")
    public ApiResponse<ProductPriceResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<ProductPriceResponse>builder()
                .success(true)
                .code(200)
                .message("Get product price success")
                .data(productPriceService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update product price (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<ProductPriceResponse> update(@PathVariable UUID id,
            @Valid @RequestBody ProductPriceUpdateRequest request) {
        return ApiResponse.<ProductPriceResponse>builder()
                .success(true)
                .code(200)
                .message("Update product price success")
                .data(productPriceService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.example.GasTuanDat.common.constants.RoleConstants).ADMIN)")
    @Operation(summary = "Delete product price (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        productPriceService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete product price success")
                .build();
    }
}
