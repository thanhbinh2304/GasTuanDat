package com.example.GasTuanDat.sale.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.sale.dtos.SaleInvoiceCreateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceResponse;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceUpdateRequest;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;

@Mapper(componentModel = "spring")
public interface SaleInvoiceMapper {

    @Mapping(target = "invoiceId", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "gasBook", ignore = true)
    @Mapping(target = "stock", ignore = true)
    SaleInvoiceEntity toEntity(SaleInvoiceCreateRequest request);

    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "customerId", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null)")
    @Mapping(target = "customerCode", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getCustomerCode() : (entity.getGasBook() != null ? entity.getGasBook().getGasBookCode() : null))")
    @Mapping(target = "customerName", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getFullName() : (entity.getGasBook() != null ? entity.getGasBook().getFullName() : null))")
    @Mapping(target = "gasBookId", expression = "java(entity.getGasBook() != null ? entity.getGasBook().getGasBookId() : null)")
    @Mapping(target = "stockId", expression = "java(entity.getStock() != null ? entity.getStock().getStockId() : null)")
    @Mapping(target = "details", ignore = true)
    SaleInvoiceResponse toResponse(SaleInvoiceEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "invoiceCode", source = "invoiceCode")
    @Mapping(target = "invoiceDate", source = "invoiceDate")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "paidAmount", source = "paidAmount")
    @Mapping(target = "orderType", source = "orderType")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    void updateEntity(SaleInvoiceUpdateRequest request, @MappingTarget SaleInvoiceEntity entity);
}
