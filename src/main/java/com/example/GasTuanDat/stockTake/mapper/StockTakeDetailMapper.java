package com.example.GasTuanDat.stockTake.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailCreateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailResponse;
import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailUpdateRequest;
import com.example.GasTuanDat.stockTake.entities.StockTakeDetailEntity;

@Mapper(componentModel = "spring")
public interface StockTakeDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stockTake", ignore = true)
    @Mapping(target = "product", ignore = true)
    StockTakeDetailEntity toEntity(StockTakeDetailCreateRequest request);

    @Mapping(target = "stockTakeId", expression = "java(entity.getStockTake() != null ? entity.getStockTake().getStockTakeId() : null)")
    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductId() : null)")
    @Mapping(target = "productName", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductName() : null)")
    StockTakeDetailResponse toResponse(StockTakeDetailEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "systemQuantity", source = "systemQuantity")
    @Mapping(target = "actualQuantity", source = "actualQuantity")
    void updateEntity(StockTakeDetailUpdateRequest request, @MappingTarget StockTakeDetailEntity entity);
}
