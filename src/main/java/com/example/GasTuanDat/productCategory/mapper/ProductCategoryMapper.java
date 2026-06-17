package com.example.GasTuanDat.productCategory.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.productCategory.dtos.ProductCategoryCreateRequest;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryResponse;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryUpdateRequest;
import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mapping(target = "categoryId", ignore = true)
    ProductCategoryEntity toEntity(ProductCategoryCreateRequest request);

    ProductCategoryResponse toResponse(ProductCategoryEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "categoryName", source = "categoryName")
    void updateEntity(ProductCategoryUpdateRequest request, @MappingTarget ProductCategoryEntity entity);
}
