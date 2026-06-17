package com.example.GasTuanDat.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.product.dtos.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCacheService {
    private final ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "products", key = "{'json', #keyword, #productCategory, #stock, #priceList, #productAttribute, #attributeValue, #page, #pageSize}", sync = true)
    public String searchAsJson(String keyword, String productCategory, String stock, String priceList,
            String productAttribute, String attributeValue, int page, int pageSize) {
        
        Page<ProductResponse> result = productService.search(
                keyword, productCategory, stock, priceList, productAttribute, attributeValue, page, pageSize);

        List<ProductResponse> data = result.getContent();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getNumber() + 1);
        pagination.put("pageSize", result.getSize());
        pagination.put("total", result.getTotalElements());
        pagination.put("totalPages", result.getTotalPages());

        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Get products success")
                .data(data)
                .pagination(pagination)
                .build();

        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }
}
