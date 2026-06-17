package com.example.GasTuanDat.stock;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.stock.dtos.StockCreateRequest;
import com.example.GasTuanDat.stock.dtos.StockResponse;
import com.example.GasTuanDat.stock.dtos.StockUpdateRequest;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.stock.mapper.StockMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public StockResponse create(StockCreateRequest request) {
        String name = normalizeName(request.getName());
        validateName(name);

        if (stockRepository.existsByNameIgnoreCase(name)) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }

        StockEntity entity = stockMapper.toEntity(request);
        entity.setName(name);

        return stockMapper.toResponse(stockRepository.save(entity));
    }

    public PageResult<StockResponse> search(String name, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        Page<StockEntity> result = name == null || name.isBlank()
                ? stockRepository.findAll(pageable)
                : stockRepository.findByNameContainingIgnoreCase(name.trim(), pageable);

        return PageResult.<StockResponse>builder()
            .content(result.getContent().stream().map(stockMapper::toResponse).toList())
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public StockResponse getById(UUID id) {
        return stockMapper.toResponse(stockRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT)));
    }

    public StockResponse update(UUID id, StockUpdateRequest request) {
        StockEntity entity = stockRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));

        stockMapper.updateEntity(request, entity);

        if (request.getName() != null) {
            String name = normalizeName(request.getName());
            validateName(name);
            stockRepository.findAll().stream()
                    .filter(stock -> stock.getName() != null && stock.getName().equalsIgnoreCase(name))
                    .filter(stock -> !stock.getStockId().equals(id))
                    .findFirst()
                    .ifPresent(existing -> {
                        throw new ApiException(ErrorCode.INVALID_INPUT);
                    });
            entity.setName(name);
        }

        if (request.getWardId() != null) {
            entity.setWardId(request.getWardId());
        }

        return stockMapper.toResponse(stockRepository.save(entity));
    }

    public void delete(UUID id) {
        StockEntity entity = stockRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
        stockRepository.delete(entity);
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }
}