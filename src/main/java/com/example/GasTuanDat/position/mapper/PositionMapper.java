package com.example.GasTuanDat.position.mapper;

import com.example.GasTuanDat.position.dtos.PositionCreationRequest;
import com.example.GasTuanDat.position.dtos.PositionResponse;
import com.example.GasTuanDat.position.dtos.PositionUpdateRequest;
import com.example.GasTuanDat.position.entities.PositionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    @Mapping(target = "positionId", ignore = true)
    @Mapping(target = "name", source = "positionName")
    PositionEntity toEntity(PositionCreationRequest request);

    @Mapping(target = "positionName", source = "name")
    PositionResponse toResponse(PositionEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "positionName")
    void updateEntity(PositionUpdateRequest request, @MappingTarget PositionEntity entity);
}