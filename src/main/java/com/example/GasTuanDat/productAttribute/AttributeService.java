package com.example.GasTuanDat.productAttribute;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.productAttribute.dtos.AttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.AttributeResponse;
import com.example.GasTuanDat.productAttribute.dtos.AttributeUpdateRequest;
import com.example.GasTuanDat.productAttribute.entities.AttributeEntity;
import com.example.GasTuanDat.productAttribute.mapper.AttributeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    public AttributeResponse create(AttributeCreateRequest request) {
        String attributeName = normalizeAttributeName(request.getAttributeName());
        validateAttributeName(attributeName);

        if (attributeRepository.findByAttributeNameIgnoreCase(attributeName).isPresent()) {
            throw new ApiException(ErrorCode.ATTRIBUTE_ALREADY_EXISTS);
        }

        AttributeEntity attribute = AttributeEntity.builder()
                .attributeName(attributeName)
                .build();

        return attributeMapper.toResponse(attributeRepository.save(attribute));
    }

    public PageResult<AttributeResponse> search(String attributeName, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("attributeName").ascending());
        Page<AttributeEntity> result = attributeRepository.searchByAttributeName(attributeName, pageable);

        return PageResult.<AttributeResponse>builder()
                .content(result.getContent().stream().map(attributeMapper::toResponse).toList())
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public AttributeResponse getById(UUID attributeId) {
        return attributeMapper.toResponse(attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTRIBUTE_NOT_FOUND)));
    }

    public AttributeResponse update(UUID attributeId, AttributeUpdateRequest request) {
        AttributeEntity attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        if (request.getAttributeName() != null) {
            String normalizedName = normalizeAttributeName(request.getAttributeName());
            validateAttributeName(normalizedName);
            attributeRepository.findByAttributeNameIgnoreCase(normalizedName)
                    .filter(existing -> !existing.getAttributeId().equals(attributeId))
                    .ifPresent(existing -> {
                        throw new ApiException(ErrorCode.ATTRIBUTE_ALREADY_EXISTS);
                    });
            attribute.setAttributeName(normalizedName);
        }

        return attributeMapper.toResponse(attributeRepository.save(attribute));
    }

    public void delete(UUID attributeId) {
        AttributeEntity attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        attributeRepository.delete(attribute);
    }

    private String normalizeAttributeName(String attributeName) {
        return attributeName == null ? null : attributeName.trim();
    }

    private void validateAttributeName(String attributeName) {
        if (attributeName == null || attributeName.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }
}
