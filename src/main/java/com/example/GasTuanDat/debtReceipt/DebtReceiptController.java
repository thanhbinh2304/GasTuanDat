package com.example.GasTuanDat.debtReceipt;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
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
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptCreateRequest;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptResponse;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/debt-receipts")
@RequiredArgsConstructor
@Tag(name = "Debt Receipt Controller", description = "Quản lý phiếu nợ")
public class DebtReceiptController {

    private final DebtReceiptService debtReceiptService;

    @Operation(summary = "Tạo mới phiếu nợ")
    @PostMapping
    public ApiResponse<DebtReceiptResponse> create(@RequestBody @Valid DebtReceiptCreateRequest request) {
        return ApiResponse.<DebtReceiptResponse>builder()
                .code(200)
                .message("Success")
                .data(debtReceiptService.create(request))
                .build();
    }

    @Operation(summary = "Lấy chi tiết phiếu nợ")
    @GetMapping("/{id}")
    public ApiResponse<DebtReceiptResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<DebtReceiptResponse>builder()
                .code(200)
                .message("Success")
                .data(debtReceiptService.getById(id))
                .build();
    }

    @Operation(summary = "Tìm kiếm phiếu nợ")
    @GetMapping
    public ApiResponse<PageResult<DebtReceiptResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueEndDate,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ApiResponse.<PageResult<DebtReceiptResponse>>builder()
                .code(200)
                .message("Success")
                .data(debtReceiptService.search(keyword, startDate, endDate, dueStartDate, dueEndDate, page, limit))
                .build();
    }

    @Operation(summary = "Cập nhật phiếu nợ")
    @PutMapping("/{id}")
    public ApiResponse<DebtReceiptResponse> update(@PathVariable UUID id, @RequestBody @Valid DebtReceiptUpdateRequest request) {
        return ApiResponse.<DebtReceiptResponse>builder()
                .code(200)
                .message("Success")
                .data(debtReceiptService.update(id, request))
                .build();
    }

    @Operation(summary = "Xóa phiếu nợ")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        debtReceiptService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Success")
                .build();
    }
}
