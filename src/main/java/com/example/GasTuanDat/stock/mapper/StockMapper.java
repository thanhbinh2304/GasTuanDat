package com.example.GasTuanDat.stock.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.stock.dtos.StockCreateRequest;
import com.example.GasTuanDat.stock.dtos.StockResponse;
import com.example.GasTuanDat.stock.dtos.StockUpdateRequest;
import com.example.GasTuanDat.stock.entities.StockEntity;

@Mapper(componentModel = "spring")
public interface StockMapper {
    @Mapping(target = "stockId", ignore = true)
    StockEntity toEntity(StockCreateRequest request);

    StockResponse toResponse(StockEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "wardId", source = "wardId")
    void updateEntity(StockUpdateRequest request, @MappingTarget StockEntity entity);
}