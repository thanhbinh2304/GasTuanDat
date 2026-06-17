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
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeResponse;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeUpdateRequest;
import com.example.GasTuanDat.productAttribute.entities.AttributeEntity;
import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;
import com.example.GasTuanDat.productAttribute.mapper.ProductAttributeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final AttributeRepository attributeRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeMapper productAttributeMapper;

    public ProductAttributeResponse create(ProductAttributeCreateRequest request) {
        UUID attributeId = request.getAttributeId();
        UUID productId = request.getProductId();
        String attributeValue = normalizeAttributeValue(request.getAttributeValue());

        validateProductAttributeRequest(attributeId, productId, attributeValue);

        AttributeEntity attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        if (productAttributeRepository.findByAttributeIdAndProductId(attributeId, productId).isPresent()) {
            throw new ApiException(ErrorCode.PRODUCT_ATTRIBUTE_ALREADY_EXISTS);
        }

        ProductAttributeEntity entity = ProductAttributeEntity.builder()
                .attribute(attribute)
                .product(product)
                .attributeValue(attributeValue)
                .build();

        return productAttributeMapper.toResponse(productAttributeRepository.save(entity));
    }

    public PageResult<ProductAttributeResponse> search(String attributeValue, Integer page, Integer limit) {
        int pageNumber = page == null || page < 0 ? 0 : page;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("attributeValue").ascending());
        Page<ProductAttributeEntity> result = productAttributeRepository.searchByAttributeValue(attributeValue, pageable);

        return PageResult.<ProductAttributeResponse>builder()
                .content(result.getContent().stream().map(productAttributeMapper::toResponse).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public ProductAttributeResponse getById(UUID id) {
        return productAttributeMapper.toResponse(productAttributeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND)));
    }

    public ProductAttributeResponse update(UUID id, ProductAttributeUpdateRequest request) {
        ProductAttributeEntity entity = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));

        UUID attributeId = request.getAttributeId();
        UUID productId = request.getProductId();
        String attributeValue = normalizeAttributeValue(request.getAttributeValue());

        validateProductAttributeRequest(attributeId, productId, attributeValue);

        AttributeEntity attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        productAttributeRepository.findByAttributeIdAndProductId(attributeId, productId)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ApiException(ErrorCode.PRODUCT_ATTRIBUTE_ALREADY_EXISTS);
                });

        entity.setAttribute(attribute);
        entity.setProduct(product);
        entity.setAttributeValue(attributeValue);

        return productAttributeMapper.toResponse(productAttributeRepository.save(entity));
    }

    public void delete(UUID id) {
        ProductAttributeEntity entity = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));
        productAttributeRepository.delete(entity);
    }

    private void validateProductAttributeRequest(UUID attributeId, UUID productId, String attributeValue) {
        if (attributeId == null || productId == null || attributeValue == null || attributeValue.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }

    private String normalizeAttributeValue(String attributeValue) {
        return attributeValue == null ? null : attributeValue.trim();
    }
}
