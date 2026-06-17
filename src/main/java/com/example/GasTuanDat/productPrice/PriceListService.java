package com.example.GasTuanDat.productPrice;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.productPrice.dtos.PriceListCreateRequest;
import com.example.GasTuanDat.productPrice.dtos.PriceListResponse;
import com.example.GasTuanDat.productPrice.dtos.PriceListUpdateRequest;
import com.example.GasTuanDat.productPrice.entities.PriceListEntity;
import com.example.GasTuanDat.productPrice.mapper.PriceListMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceListService {
    private final PriceListRepository priceListRepository;
    private final PriceListMapper priceListMapper;

    public PriceListResponse create(PriceListCreateRequest request) {
        String name = normalizeName(request.getPriceListName());
        if (priceListRepository.findByPriceListNameIgnoreCase(name).isPresent()) {
            throw new ApiException(ErrorCode.PRICE_LIST_ALREADY_EXISTS);
        }

        PriceListEntity entity = PriceListEntity.builder()
                .priceListName(name)
                .build();

        return priceListMapper.toResponse(priceListRepository.save(entity));
    }

    public PageResult<PriceListResponse> search(String name, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("priceListName").ascending());
        Page<PriceListEntity> result = priceListRepository.searchByName(name, pageable);

        return PageResult.<PriceListResponse>builder()
                .content(result.getContent().stream().map(priceListMapper::toResponse).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public PriceListResponse getById(UUID id) {
        return priceListMapper.toResponse(priceListRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRICE_LIST_NOT_FOUND)));
    }

    public PriceListResponse update(UUID id, PriceListUpdateRequest request) {
        PriceListEntity entity = priceListRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRICE_LIST_NOT_FOUND));

        String name = normalizeName(request.getPriceListName());
        priceListRepository.findByPriceListNameIgnoreCase(name)
                .filter(existing -> !existing.getPriceListId().equals(id))
                .ifPresent(existing -> {
                    throw new ApiException(ErrorCode.PRICE_LIST_ALREADY_EXISTS);
                });

        entity.setPriceListName(name);
        return priceListMapper.toResponse(priceListRepository.save(entity));
    }

    public void delete(UUID id) {
        PriceListEntity entity = priceListRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRICE_LIST_NOT_FOUND));
        priceListRepository.delete(entity);
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }
}
