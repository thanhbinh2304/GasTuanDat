package com.example.GasTuanDat.gasBook;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.gasBook.dtos.GasBookCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.GasBookResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookUpdateRequest;
import com.example.GasTuanDat.gasBook.dtos.GasBookHistoryResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookDetailedHistoryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/gasbooks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "GasBook", description = "GasBook management APIs")
public class GasBookController {
    GasBookService gasBookService;

    @PostMapping
    @Operation(summary = "Create GasBook")
    public ApiResponse<GasBookResponse> create(@Valid @RequestBody GasBookCreateRequest request) {
        return ApiResponse.<GasBookResponse>builder()
                .success(true)
                .code(200)
                .message("Create GasBook success")
                .data(gasBookService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get GasBook list")
    public ApiResponse<PageResult<GasBookResponse>> getAll(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<GasBookResponse> result = gasBookService.getAll(page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<PageResult<GasBookResponse>>builder()
                .success(true)
                .code(200)
                .message("Get GasBook list success")
                .data(result)
                .pagination(pagination)
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search GasBook with pagination")
    public ApiResponse<PageResult<GasBookResponse>> searchGasBook(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String gasBookId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String customerGroup,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<GasBookResponse> result = gasBookService.search(keyword, gasBookId, fullName, phoneNumber, customerGroup, page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<PageResult<GasBookResponse>>builder()
                .success(true)
                .code(200)
                .message("Search GasBook success")
                .data(result)
                .pagination(pagination)
                .build();
    }

    @GetMapping("/{gasBookId}")
    @Operation(summary = "Get GasBook detail by id")
    public ApiResponse<GasBookResponse> getById(@PathVariable UUID gasBookId) {
        return ApiResponse.<GasBookResponse>builder()
                .success(true)
                .code(200)
                .message("Get GasBook success")
                .data(gasBookService.getById(gasBookId))
                .build();
    }

    @PutMapping("/{gasBookId}")
    @Operation(summary = "Update GasBook")
    public ApiResponse<GasBookResponse> update(@PathVariable UUID gasBookId,
            @Valid @RequestBody GasBookUpdateRequest request) {
        return ApiResponse.<GasBookResponse>builder()
                .success(true)
                .code(200)
                .message("Update GasBook success")
                .data(gasBookService.update(gasBookId, request))
                .build();
    }

    @DeleteMapping("/{gasBookId}")
    @Operation(summary = "Delete GasBook")
    public ApiResponse<Void> delete(@PathVariable UUID gasBookId) {
        gasBookService.delete(gasBookId);
        return ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Delete GasBook success")
                .build();
    }

    @GetMapping("/{gasBookId}/history")
    @Operation(summary = "Get GasBook history")
    public ApiResponse<PageResult<GasBookHistoryResponse>> getHistory(
            @PathVariable UUID gasBookId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<GasBookHistoryResponse> result = gasBookService.getHistory(gasBookId, page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<PageResult<GasBookHistoryResponse>>builder()
                .success(true)
                .code(200)
                .message("Get GasBook history success")
                .data(result)
                .pagination(pagination)
                .build();
    }

    @GetMapping("/{gasBookId}/detailed-history")
    @Operation(summary = "Get GasBook detailed history")
    public ApiResponse<PageResult<GasBookDetailedHistoryResponse>> getDetailedHistory(
            @PathVariable UUID gasBookId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit
    ) {
        PageResult<GasBookDetailedHistoryResponse> result = gasBookService.getDetailedHistory(gasBookId, page, limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getPage());
        pagination.put("limit", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<PageResult<GasBookDetailedHistoryResponse>>builder()
                .success(true)
                .code(200)
                .message("Get GasBook detailed history success")
                .data(result)
                .pagination(pagination)
                .build();
    }
}
