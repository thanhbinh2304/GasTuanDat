package com.example.GasTuanDat.stock;

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
import com.example.GasTuanDat.stock.dtos.StockCreateRequest;
import com.example.GasTuanDat.stock.dtos.StockResponse;
import com.example.GasTuanDat.stock.dtos.StockUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Stock", description = "Stock management APIs")
public class StockController {
    StockService stockService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create stock (WAREHOUSE, ADMIN)")
    public ApiResponse<StockResponse> create(@Valid @RequestBody StockCreateRequest request) {
        return ApiResponse.<StockResponse>builder()
                .success(true)
                .code(200)
                .message("Create stock success")
                .data(stockService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all stocks")
    public ApiResponse<java.util.List<StockResponse>> getAll() {
        PageResult<StockResponse> result = stockService.search(null, 1, 1000);
        return ApiResponse.<java.util.List<StockResponse>>builder()
                .success(true)
                .code(200)
                .message("Get stocks success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search stocks with pagination")
    public ApiResponse<java.util.List<StockResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer limit) {
        PageResult<StockResponse> result = stockService.search(name, page, limit);
        return ApiResponse.<java.util.List<StockResponse>>builder()
                .success(true)
                .code(200)
                .message("Search stocks success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock by id")
    public ApiResponse<StockResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<StockResponse>builder()
                .success(true)
                .code(200)
                .message("Get stock success")
                .data(stockService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update stock (WAREHOUSE, ADMIN)")
    public ApiResponse<StockResponse> update(@PathVariable UUID id,
            @Valid @RequestBody StockUpdateRequest request) {
        return ApiResponse.<StockResponse>builder()
                .success(true)
                .code(200)
                .message("Update stock success")
                .data(stockService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.example.GasTuanDat.common.constants.RoleConstants).ADMIN)")
    @Operation(summary = "Delete stock (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        stockService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete stock success")
                .build();
    }
}