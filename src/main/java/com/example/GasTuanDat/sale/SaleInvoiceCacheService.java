package com.example.GasTuanDat.sale;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleInvoiceCacheService {
    private final SaleInvoiceService saleInvoiceService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Cacheable(value = "sale_invoices", key = "{'json', #keyword, #startDate, #endDate, #customerId, #stockId, #employeeId, #orderType, #customerGroupId, #page, #limit}", sync = true)
    public String searchAsJson(String keyword, OffsetDateTime startDate, OffsetDateTime endDate, UUID customerId,
            UUID stockId, UUID employeeId, String orderType, UUID customerGroupId, int page, int limit) {

        PageResult<SaleInvoiceResponse> result = saleInvoiceService.search(
                keyword, startDate, endDate, customerId, stockId, employeeId, orderType, customerGroupId, page, limit);

        ApiResponse<PageResult<SaleInvoiceResponse>> response = ApiResponse.<PageResult<SaleInvoiceResponse>>builder()
                .code(200)
                .message("Success")
                .data(result)
                .build();

        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }
}
