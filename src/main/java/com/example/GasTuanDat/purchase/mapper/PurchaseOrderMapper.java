package com.example.GasTuanDat.purchase.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.purchase.dtos.PurchaseOrderCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderUpdateRequest;
import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(target = "purchaseId", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "stock", ignore = true)
    PurchaseOrderEntity toEntity(PurchaseOrderCreateRequest request);

    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "employeeName", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getFullName() : null)")
    @Mapping(target = "supplierId", expression = "java(entity.getSupplier() != null ? entity.getSupplier().getSupplierId() : null)")
    @Mapping(target = "supplierCode", expression = "java(entity.getSupplier() != null ? \"NCC\" + entity.getSupplier().getSupplierId().toString().substring(0, 6).toUpperCase() : null)")
    @Mapping(target = "supplierName", expression = "java(entity.getSupplier() != null ? entity.getSupplier().getFullName() : null)")
    @Mapping(target = "stockId", expression = "java(entity.getStock() != null ? entity.getStock().getStockId() : null)")
    @Mapping(target = "stockName", expression = "java(entity.getStock() != null ? entity.getStock().getName() : null)")
    PurchaseOrderResponse toResponse(PurchaseOrderEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "purchaseCode", source = "purchaseCode")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "paidAmount", source = "paidAmount")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "orderType", source = "orderType")
    void updateEntity(PurchaseOrderUpdateRequest request, @MappingTarget PurchaseOrderEntity entity);
}
