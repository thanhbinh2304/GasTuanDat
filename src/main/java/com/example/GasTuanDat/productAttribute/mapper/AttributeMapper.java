package com.example.GasTuanDat.productAttribute.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.productAttribute.dtos.AttributeUpdateRequest;
import com.example.GasTuanDat. productAttribute.dtos.AttributeCreateRequest;
import com.example.GasTuanDat.productAttribute.dtos.AttributeResponse;
import com.example.GasTuanDat.productAttribute.entities.AttributeEntity;


@Mapper(componentModel = "spring")
public interface AttributeMapper {
    @Mapping(target = "attributeId", ignore = true)

    AttributeEntity toEntity(AttributeCreateRequest request);
    AttributeResponse toResponse(AttributeEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "attributeId", ignore = true)
    @Mapping(target = "attributeName", source = "attributeName")
    void updateEntity(AttributeUpdateRequest request, @MappingTarget AttributeEntity entity);
    
}
