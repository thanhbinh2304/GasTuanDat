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
import com.example.GasTuanDat.productPrice.dtos.PriceListCreateRequest;
import com.example.GasTuanDat.productPrice.dtos.PriceListResponse;
import com.example.GasTuanDat.productPrice.dtos.PriceListUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/price-list")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "PriceList", description = "Price list management APIs")
public class PriceListController {
    PriceListService priceListService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create price list (NHÂN VIÊN KHO, ADMIN)")
    
    
    public ApiResponse<PriceListResponse> create(@Valid @RequestBody PriceListCreateRequest request) {
        return ApiResponse.<PriceListResponse>builder()
                .success(true)
                .code(200)
                .message("Create price list success")
                .data(priceListService.create(request))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search price lists with pagination")
    public ApiResponse<java.util.List<PriceListResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer limit) {
        PageResult<PriceListResponse> result = priceListService.search(name, page, limit);
        return ApiResponse.<java.util.List<PriceListResponse>>builder()
                .success(true)
                .code(200)
                .message("Search price lists success")
                .data(result.getContent())
                .pagination(Map.of(
                        "page", result.getPage(),
                        "limit", result.getSize(),
                        "total", result.getTotalElements(),
                        "totalPages", result.getTotalPages()))
                .build();
    }

    @GetMapping("/{priceListId}")
    @Operation(summary = "Get price list by id")
    public ApiResponse<PriceListResponse> getById(@PathVariable UUID priceListId) {
        return ApiResponse.<PriceListResponse>builder()
                .success(true)
                .code(200)
                .message("Get price list success")
                .data(priceListService.getById(priceListId))
                .build();
    }

    @PutMapping("/{priceListId}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update price list (NHÂN VIÊN KHO, ADMIN)")
    public ApiResponse<PriceListResponse> update(@PathVariable UUID priceListId,
            @Valid @RequestBody PriceListUpdateRequest request) {
        return ApiResponse.<PriceListResponse>builder()
                .success(true)
                .code(200)
                .message("Update price list success")
                .data(priceListService.update(priceListId, request))
                .build();
    }

    @DeleteMapping("/{priceListId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete price list (ADMIN only)")
    public ApiResponse<Void> delete(@PathVariable UUID priceListId) {
        priceListService.delete(priceListId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete price list success")
                .build();
    }
}
