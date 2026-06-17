package com.example.GasTuanDat.productPrice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.GasTuanDat.productPrice.dtos.ProductPriceResponse;
import com.example.GasTuanDat.productPrice.entities.ProductPriceEntity;

@Mapper(componentModel = "spring")
public interface ProductPriceMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "priceListId", source = "priceList.priceListId")
    @Mapping(target = "priceListName", source = "priceList.priceListName")
    ProductPriceResponse toResponse(ProductPriceEntity entity);
}
