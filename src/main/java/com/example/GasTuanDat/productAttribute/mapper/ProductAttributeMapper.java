package com.example.GasTuanDat.productAttribute.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeResponse;
import com.example.GasTuanDat.productAttribute.dtos.ProductAttributeUpdateRequest;
import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {

    ProductAttributeEntity toEntity(ProductAttributeCreateRequest request);

    @Mapping(source = "attribute.attributeId", target = "attributeId")
    @Mapping(source = "product.productId", target = "productId")
    ProductAttributeResponse toResponse(ProductAttributeEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeValue", source = "attributeValue")
    void updateEntity(
            ProductAttributeUpdateRequest request,
            @MappingTarget ProductAttributeEntity entity
    );
}