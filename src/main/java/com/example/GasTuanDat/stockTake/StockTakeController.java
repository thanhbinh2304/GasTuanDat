package com.example.GasTuanDat.stockTake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.example.GasTuanDat.stockTake.dtos.StockTakeCreateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeResponse;
import com.example.GasTuanDat.stockTake.dtos.StockTakeUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/stock-takes")
public class StockTakeController {

    @Autowired
    private StockTakeService stockTakeService;

    @GetMapping
    public ApiResponse<List<StockTakeResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String employee,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int pageSize) {
        int pageNumber = page > 0 ? page - 1 : 0;
        Page<StockTakeResponse> result = stockTakeService.search(
                keyword, startDate, endDate, stock, employee, pageNumber, pageSize);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", page);
        pagination.put("pageSize", pageSize);
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        return ApiResponse.<List<StockTakeResponse>>builder()
                .code(200)
                .message("Get stock takes success")
                .data(result.getContent())
                .pagination(pagination)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StockTakeResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<StockTakeResponse>builder().code(200).message("Get stock take by ID success").data(stockTakeService.getById(id)).build();
    }

    @PostMapping
    public ApiResponse<StockTakeResponse> create(@Valid @RequestBody StockTakeCreateRequest request) {
        return ApiResponse.<StockTakeResponse>builder().code(200).message("Create stock take success").data(stockTakeService.create(request)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<StockTakeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody StockTakeUpdateRequest request) {
        return ApiResponse.<StockTakeResponse>builder().code(200).message("Update stock take success").data(stockTakeService.update(id, request)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        stockTakeService.delete(id);
        return ApiResponse.<Void>builder().code(200).message("Delete stock take success").build();
    }
}
