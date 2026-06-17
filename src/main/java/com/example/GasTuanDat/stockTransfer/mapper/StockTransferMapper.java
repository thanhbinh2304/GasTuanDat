package com.example.GasTuanDat.stockTransfer.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.stockTransfer.dtos.StockTransferCreateRequest;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferDetailResponse;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferResponse;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferUpdateRequest;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferEntity;

@Mapper(componentModel = "spring")
public interface StockTransferMapper {

    @Mapping(target = "transferId", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "fromStock", ignore = true)
    @Mapping(target = "toStock", ignore = true)
    StockTransferEntity toEntity(StockTransferCreateRequest request);

    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "employeeName", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getFullName() : null)")
    @Mapping(target = "fromStockId", expression = "java(entity.getFromStock() != null ? entity.getFromStock().getStockId() : null)")
    @Mapping(target = "fromStockName", expression = "java(entity.getFromStock() != null ? entity.getFromStock().getName() : null)")
    @Mapping(target = "toStockId", expression = "java(entity.getToStock() != null ? entity.getToStock().getStockId() : null)")
    @Mapping(target = "toStockName", expression = "java(entity.getToStock() != null ? entity.getToStock().getName() : null)")
    @Mapping(target = "details", expression = "java(mapDetails(entity))")
    StockTransferResponse toResponse(StockTransferEntity entity);

    default List<StockTransferDetailResponse> mapDetails(StockTransferEntity entity) {
        if (entity.getDetails() == null) {
            return null;
        }
        return entity.getDetails().stream()
                .map(this::mapDetail)
                .toList();
    }

    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductId() : null)")
    @Mapping(target = "productName", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductName() : null)")
    StockTransferDetailResponse mapDetail(com.example.GasTuanDat.stockTransfer.entities.StockTransferDetailEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "transferCode", source = "transferCode")
    @Mapping(target = "transferDate", source = "transferDate")
    @Mapping(target = "note", source = "note")
    void updateEntity(StockTransferUpdateRequest request, @MappingTarget StockTransferEntity entity);
}
