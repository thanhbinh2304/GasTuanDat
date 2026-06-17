package com.example.GasTuanDat.product;

import static com.example.GasTuanDat.common.constants.RoleConstants.ADMIN;
import static com.example.GasTuanDat.common.constants.RoleConstants.WAREHOUSE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import com.example.GasTuanDat.product.dtos.ProductCreateRequest;
import com.example.GasTuanDat.product.dtos.ProductResponse;
import com.example.GasTuanDat.product.dtos.ProductUpdateRequest;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.product.mapper.ProductMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Product management APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
        ProductService productService;
        ProductMapper productMapper;

        ProductCacheService productCacheService;

        @PostMapping
        @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
        @Operation(summary = "Create product (WAREHOUSE, ADMIN)")
        public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
                ProductEntity created = productService.create(request);
                return ApiResponse.<ProductResponse>builder().code(200).message("Create product success")
                                .data(productMapper.toResponse(created)).build();
        }

        @GetMapping(produces = "application/json;charset=utf-8")
        @Operation(summary = "Get products with filters")
        public String getAll(
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String productCategory,
                        @RequestParam(required = false) String stock,
                        @RequestParam(required = false) String priceList,
                        @RequestParam(required = false) String productAttribute,
                        @RequestParam(required = false) String attributeValue,

                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int pageSize) {
                int normalizedPage = Math.max(0, page - 1);
                int normalizedPageSize = Math.max(1, pageSize);

                return productCacheService.searchAsJson(
                                keyword,
                                productCategory,
                                stock,
                                priceList,
                                productAttribute,
                                attributeValue,
                                normalizedPage,
                                normalizedPageSize);
        }

        @GetMapping("/search")
        @Operation(summary = "Search products by name")
        public ApiResponse<List<ProductResponse>> search(
                        @RequestParam(required = false) String productName,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int limit) {

                int normalizedPage = Math.max(0, page - 1);
                int normalizedLimit = Math.max(1, limit);

                Page<ProductResponse> result = productService.search(productName, normalizedPage, normalizedLimit);
                List<ProductResponse> data = result.getContent();

                Map<String, Object> pagination = new HashMap<>();
                pagination.put("page", result.getNumber() + 1);
                pagination.put("limit", result.getSize());
                pagination.put("total", result.getTotalElements());
                pagination.put("totalPages", result.getTotalPages());

                return ApiResponse.<List<ProductResponse>>builder().code(200).message("Search products success")
                                .data(data).pagination(pagination).build();
        }

        @GetMapping("/{productId:[0-9a-fA-F\\-]{36}}")
        @Operation(summary = "Get product detail")
        public ApiResponse<ProductResponse> getById(@PathVariable UUID productId) {
                ProductEntity entity = productService.getById(productId);
                return ApiResponse.<ProductResponse>builder().code(200).message("Get product success")
                                .data(productMapper.toResponse(entity)).build();
        }

        @PutMapping("/{productId:[0-9a-fA-F\\-]{36}}")
        @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
        @Operation(summary = "Update product (WAREHOUSE, ADMIN)")
        public ApiResponse<ProductResponse> update(@PathVariable UUID productId,
                        @Valid @RequestBody ProductUpdateRequest request) {
                ProductEntity updated = productService.update(productId, request);
                return ApiResponse.<ProductResponse>builder().code(200).message("Update product success")
                                .data(productMapper.toResponse(updated)).build();
        }

        @DeleteMapping("/{productId:[0-9a-fA-F\\-]{36}}")
        @PreAuthorize("hasAnyRole('" + ADMIN + "')")
        @Operation(summary = "Delete product (only ADMIN)")
        public ApiResponse<Void> delete(@PathVariable UUID productId) {
                productService.delete(productId);
                return ApiResponse.<Void>builder().code(200).message("Delete product success").build();
        }
}
