package com.example.GasTuanDat.stockTake.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.stockTake.dtos.StockTakeCreateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeResponse;
import com.example.GasTuanDat.stockTake.dtos.StockTakeUpdateRequest;
import com.example.GasTuanDat.stockTake.entities.StockTakeEntity;

@Mapper(componentModel = "spring")
public interface StockTakeMapper {

    @Mapping(target = "stockTakeId", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "stock", ignore = true)
    StockTakeEntity toEntity(StockTakeCreateRequest request);

    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "stockId", expression = "java(entity.getStock() != null ? entity.getStock().getStockId() : null)")
    StockTakeResponse toResponse(StockTakeEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "stockTakeCode", source = "stockTakeCode")
    @Mapping(target = "stockTakeDate", source = "stockTakeDate")
    @Mapping(target = "note", source = "note")
    void updateEntity(StockTakeUpdateRequest request, @MappingTarget StockTakeEntity entity);
}
