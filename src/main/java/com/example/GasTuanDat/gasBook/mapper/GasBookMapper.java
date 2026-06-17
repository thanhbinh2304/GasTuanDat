package com.example.GasTuanDat.gasBook.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.gasBook.dtos.GasBookCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.GasBookResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookUpdateRequest;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;

@Mapper(componentModel = "spring")
public interface GasBookMapper {

    @Mapping(target = "gasBookId", ignore = true)
    @Mapping(target = "ward", ignore = true)
    @Mapping(target = "customerGroup", ignore = true)
    GasBookEntity toEntity(GasBookCreateRequest request);

    @Mapping(target = "wardId", expression = "java(entity.getWard() != null ? entity.getWard().getWardId() : null)")
    @Mapping(target = "wardName", expression = "java(entity.getWard() != null ? entity.getWard().getWardName() : null)")
    @Mapping(target = "areaId", expression = "java((entity.getWard() != null && entity.getWard().getArea() != null) ? entity.getWard().getArea().getAreaId() : null)")
    @Mapping(target = "areaName", expression = "java((entity.getWard() != null && entity.getWard().getArea() != null) ? entity.getWard().getArea().getAreaName() : null)")
    @Mapping(target = "customerGroupId", expression = "java(entity.getCustomerGroup() != null ? entity.getCustomerGroup().getCustomerGroupId() : null)")
    @Mapping(target = "customerGroupName", expression = "java(entity.getCustomerGroup() != null ? entity.getCustomerGroup().getGroupName() : null)")
    GasBookResponse toResponse(GasBookEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "gasBookCode", source = "gasBookCode")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "points", source = "points")
    @Mapping(target = "cycle", source = "cycle")
    void updateEntity(GasBookUpdateRequest request, @MappingTarget GasBookEntity entity);
}
