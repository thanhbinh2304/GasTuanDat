package com.example.GasTuanDat.purchase;

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
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/purchase-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "PurchaseDetail", description = "Purchase detail management APIs")
public class PurchaseDetailController {
    PurchaseDetailService purchaseDetailService;

    @PostMapping
    @Operation(summary = "Create purchase detail")
    public ApiResponse<PurchaseDetailResponse> create(@Valid @RequestBody PurchaseDetailCreateRequest request) {
        return ApiResponse.<PurchaseDetailResponse>builder()
                .success(true)
                .code(200)
                .message("Create purchase detail success")
                .data(purchaseDetailService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all purchase details")
    public ApiResponse<List<PurchaseDetailResponse>> getAll() {
        return ApiResponse.<List<PurchaseDetailResponse>>builder()
                .success(true)
                .code(200)
                .message("Get purchase details success")
                .data(purchaseDetailService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase detail by id")
    public ApiResponse<PurchaseDetailResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<PurchaseDetailResponse>builder()
                .success(true)
                .code(200)
                .message("Get purchase detail success")
                .data(purchaseDetailService.getById(id))
                .build();
    }

    @GetMapping("/order/{purchaseId}")
    @Operation(summary = "Get purchase details by purchase order id")
    public ApiResponse<List<PurchaseDetailResponse>> getByPurchaseId(@PathVariable UUID purchaseId) {
        return ApiResponse.<List<PurchaseDetailResponse>>builder()
                .success(true)
                .code(200)
                .message("Get purchase details success")
                .data(purchaseDetailService.getByPurchaseId(purchaseId))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update purchase detail")
    public ApiResponse<PurchaseDetailResponse> update(@PathVariable UUID id,
            @Valid @RequestBody PurchaseDetailUpdateRequest request) {
        return ApiResponse.<PurchaseDetailResponse>builder()
                .success(true)
                .code(200)
                .message("Update purchase detail success")
                .data(purchaseDetailService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete purchase detail")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        purchaseDetailService.delete(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete purchase detail success")
                .build();
    }
}
