package com.example.GasTuanDat.sale;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.GasTuanDat.sale.dtos.SaleInvoiceCreateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceResponse;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sale-invoices")
@RequiredArgsConstructor
public class SaleInvoiceController {

    private final SaleInvoiceService saleInvoiceService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<SaleInvoiceResponse>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID stockId,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) UUID customerGroupId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        PageResult<SaleInvoiceResponse> result = saleInvoiceService.search(
                keyword, startDate, endDate, customerId, stockId, employeeId, orderType, customerGroupId, page, limit);

        return ResponseEntity.ok(ApiResponse.<PageResult<SaleInvoiceResponse>>builder()
                .code(200)
                .message("Success")
                .data(result)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleInvoiceResponse>> getById(@PathVariable UUID id) {
        SaleInvoiceResponse response = saleInvoiceService.getById(id);
        return ResponseEntity.ok(ApiResponse.<SaleInvoiceResponse>builder()
                .code(200)
                .message("Success")
                .data(response)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleInvoiceResponse>> create(@Valid @RequestBody SaleInvoiceCreateRequest request) {
        SaleInvoiceResponse response = saleInvoiceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<SaleInvoiceResponse>builder()
                .code(201)
                .message("Created successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleInvoiceResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody SaleInvoiceUpdateRequest request) {
        SaleInvoiceResponse response = saleInvoiceService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<SaleInvoiceResponse>builder()
                .code(200)
                .message("Updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        saleInvoiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted successfully")
                .build());
    }
}
