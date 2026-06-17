package com.example.GasTuanDat.purchase;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "PurchaseOrder", description = "Purchase order management APIs")
public class PurchaseOrderController {
    PurchaseOrderService purchaseOrderService;

    @PostMapping
    @Operation(summary = "Create purchase order")
    public ApiResponse<PurchaseOrderResponse> create(@Valid @RequestBody PurchaseOrderCreateRequest request) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .success(true)
                .code(200)
                .message("Create purchase order success")
                .data(purchaseOrderService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get purchase order list")
    public ApiResponse<List<PurchaseOrderResponse>> getAll(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<PurchaseOrderResponse> result = purchaseOrderService.getAll(page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .success(true)
                .code(200)
                .message("Get purchase order list success")
                .data(result.getContent())
                .pagination(pagination)
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search purchase orders with pagination")
    public ApiResponse<List<PurchaseOrderResponse>> searchPurchaseOrder(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) UUID supplierId,
            @RequestParam(required = false) UUID stockId,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) String orderType,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<PurchaseOrderResponse> result = purchaseOrderService.search(keyword, startDate, endDate, supplierId, stockId, employeeId, orderType, page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<List<PurchaseOrderResponse>>builder()
                .success(true)
                .code(200)
                .message("Search purchase order success")
                .data(result.getContent())
                .pagination(pagination)
                .build();
    }

    @GetMapping("/{purchaseId}")
    @Operation(summary = "Get purchase order detail by id")
    public ApiResponse<PurchaseOrderResponse> getById(@PathVariable UUID purchaseId) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .success(true)
                .code(200)
                .message("Get purchase order success")
                .data(purchaseOrderService.getById(purchaseId))
                .build();
    }

    @PutMapping("/{purchaseId}")
    @Operation(summary = "Update purchase order")
    public ApiResponse<PurchaseOrderResponse> update(@PathVariable UUID purchaseId,
            @Valid @RequestBody PurchaseOrderUpdateRequest request) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .success(true)
                .code(200)
                .message("Update purchase order success")
                .data(purchaseOrderService.update(purchaseId, request))
                .build();
    }

    @DeleteMapping("/{purchaseId}")
    @Operation(summary = "Delete purchase order")
    public ApiResponse<Void> delete(@PathVariable UUID purchaseId) {
        purchaseOrderService.delete(purchaseId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete purchase order success")
                .build();
    }
}
