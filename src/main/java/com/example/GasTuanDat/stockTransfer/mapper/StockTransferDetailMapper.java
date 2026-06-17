package com.example.GasTuanDat.stockTransfer.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.stockTransfer.dtos.StockTransferDetailCreateRequest;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferDetailResponse;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferDetailUpdateRequest;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferDetailEntity;

@Mapper(componentModel = "spring")
public interface StockTransferDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transfer", ignore = true)
    @Mapping(target = "product", ignore = true)
    StockTransferDetailEntity toEntity(StockTransferDetailCreateRequest request);

    @Mapping(target = "transferId", expression = "java(entity.getTransfer() != null ? entity.getTransfer().getTransferId() : null)")
    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductId() : null)")
    @Mapping(target = "productName", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductName() : null)")
    StockTransferDetailResponse toResponse(StockTransferDetailEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "quantity", source = "quantity")
    void updateEntity(StockTransferDetailUpdateRequest request, @MappingTarget StockTransferDetailEntity entity);
}
