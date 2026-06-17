package com.example.GasTuanDat.productPrice.mapper;

import org.mapstruct.Mapper;

import com.example.GasTuanDat.productPrice.dtos.PriceListResponse;
import com.example.GasTuanDat.productPrice.entities.PriceListEntity;

@Mapper(componentModel = "spring")
public interface PriceListMapper {
    PriceListResponse toResponse(PriceListEntity entity);
}
