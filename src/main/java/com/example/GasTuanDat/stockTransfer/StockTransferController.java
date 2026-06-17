package com.example.GasTuanDat.stockTransfer;

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
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferCreateRequest;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferResponse;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferUpdateRequest;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferEntity;
import com.example.GasTuanDat.stockTransfer.mapper.StockTransferMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/stock-transfers")
@Tag(name = "Stock Transfer", description = "Stock Transfer management APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockTransferController {
    StockTransferService stockTransferService;
    StockTransferMapper stockTransferMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Create stock transfer (WAREHOUSE, ADMIN)")
    public ApiResponse<StockTransferResponse> create(@Valid @RequestBody StockTransferCreateRequest request) {
        StockTransferEntity created = stockTransferService.create(request);
        return ApiResponse.<StockTransferResponse>builder().code(200).message("Create stock transfer success")
                .data(stockTransferMapper.toResponse(created)).build();
    }

    @GetMapping
    @Operation(summary = "Get stock transfers with filters")
    public ApiResponse<List<StockTransferResponse>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String fromStock,
            @RequestParam(required = false) String toStock,
            @RequestParam(required = false) String creator,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        int normalizedPage = Math.max(0, page - 1);
        int normalizedPageSize = Math.max(1, pageSize);

        Page<StockTransferEntity> result = stockTransferService.search(
                keyword,
                startDate,
                endDate,
                fromStock,
                toStock,
                creator,
                normalizedPage,
                normalizedPageSize);
        List<StockTransferResponse> data = result.getContent().stream().map(stockTransferMapper::toResponse).toList();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getNumber() + 1);
        pagination.put("pageSize", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<List<StockTransferResponse>>builder().code(200).message("Get stock transfers success")
                .data(data).pagination(pagination).build();
    }

    @GetMapping("/{transferId:[0-9a-fA-F\\-]{36}}")
    @Operation(summary = "Get stock transfer detail")
    public ApiResponse<StockTransferResponse> getById(@PathVariable UUID transferId) {
        StockTransferEntity entity = stockTransferService.getById(transferId);
        return ApiResponse.<StockTransferResponse>builder().code(200).message("Get stock transfer success")
                .data(stockTransferMapper.toResponse(entity)).build();
    }

    @PutMapping("/{transferId:[0-9a-fA-F\\-]{36}}")
    @PreAuthorize("hasAnyRole('" + WAREHOUSE + "', '" + ADMIN + "')")
    @Operation(summary = "Update stock transfer (WAREHOUSE, ADMIN)")
    public ApiResponse<StockTransferResponse> update(@PathVariable UUID transferId,
            @Valid @RequestBody StockTransferUpdateRequest request) {
        StockTransferEntity updated = stockTransferService.update(transferId, request);
        return ApiResponse.<StockTransferResponse>builder().code(200).message("Update stock transfer success")
                .data(stockTransferMapper.toResponse(updated)).build();
    }

    @DeleteMapping("/{transferId:[0-9a-fA-F\\-]{36}}")
    @PreAuthorize("hasAnyRole('" + ADMIN + "')")
    @Operation(summary = "Delete stock transfer (only ADMIN)")
    public ApiResponse<Void> delete(@PathVariable UUID transferId) {
        stockTransferService.delete(transferId);
        return ApiResponse.<Void>builder().code(200).message("Delete stock transfer success").build();
    }
}