package com.example.GasTuanDat.productPrice;

import java.math.BigDecimal;
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
import com.example.GasTuanDat.productPrice.dtos.ProductPriceCreateRequest;
import com.example.GasTuanDat.productPrice.dtos.ProductPriceResponse;
import com.example.GasTuanDat.productPrice.dtos.ProductPriceUpdateRequest;
import com.example.GasTuanDat.productPrice.entities.PriceListEntity;
import com.example.GasTuanDat.productPrice.entities.ProductPriceEntity;
import com.example.GasTuanDat.productPrice.mapper.ProductPriceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPriceService {
    private final ProductPriceRepository productPriceRepository;
    private final PriceListRepository priceListRepository;
    private final ProductRepository productRepository;
    private final ProductPriceMapper productPriceMapper;

    public ProductPriceResponse create(ProductPriceCreateRequest request) {
        validateRequest(request.getProductId(), request.getPriceListId(), request.getSellingPrice());

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        PriceListEntity priceList = priceListRepository.findById(request.getPriceListId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRICE_LIST_NOT_FOUND));

        productPriceRepository.findAll().stream()
                .filter(pp -> pp.getProduct().getProductId().equals(product.getProductId())
                        && pp.getPriceList().getPriceListId().equals(priceList.getPriceListId()))
                .findAny()
                .ifPresent(existing -> {
                    throw new ApiException(ErrorCode.PRODUCT_PRICE_ALREADY_EXISTS);
                });

        ProductPriceEntity entity = ProductPriceEntity.builder()
                .product(product)
                .priceList(priceList)
                .sellingPrice(request.getSellingPrice())
                .build();

        return productPriceMapper.toResponse(productPriceRepository.save(entity));
    }

    public PageResult<ProductPriceResponse> search(String name, Integer page, Integer limit) {
        int pageNumber = page == null || page < 0 ? 0 : page;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("sellingPrice").ascending());
        Page<ProductPriceEntity> result = productPriceRepository.searchByProductOrPriceListName(name, pageable);

        return PageResult.<ProductPriceResponse>builder()
                .content(result.getContent().stream().map(productPriceMapper::toResponse).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public ProductPriceResponse getById(UUID id) {
        return productPriceMapper.toResponse(productPriceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_PRICE_NOT_FOUND)));
    }

    public ProductPriceResponse update(UUID id, ProductPriceUpdateRequest request) {
        ProductPriceEntity entity = productPriceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_PRICE_NOT_FOUND));

        validateRequest(request.getProductId(), request.getPriceListId(), request.getSellingPrice());

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        PriceListEntity priceList = priceListRepository.findById(request.getPriceListId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRICE_LIST_NOT_FOUND));

        productPriceRepository.findAll().stream()
                .filter(pp -> pp.getProduct().getProductId().equals(product.getProductId())
                        && pp.getPriceList().getPriceListId().equals(priceList.getPriceListId())
                        && !pp.getId().equals(id))
                .findAny()
                .ifPresent(existing -> {
                    throw new ApiException(ErrorCode.PRODUCT_PRICE_ALREADY_EXISTS);
                });

        entity.setProduct(product);
        entity.setPriceList(priceList);
        entity.setSellingPrice(request.getSellingPrice());

        return productPriceMapper.toResponse(productPriceRepository.save(entity));
    }

    public void delete(UUID id) {
        ProductPriceEntity entity = productPriceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_PRICE_NOT_FOUND));
        productPriceRepository.delete(entity);
    }

    private void validateRequest(UUID productId, UUID priceListId, BigDecimal price) {
        if (productId == null || priceListId == null || price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }
}
