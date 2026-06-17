package com.example.GasTuanDat.payment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.payment.dtos.PaymentCreateRequest;
import com.example.GasTuanDat.payment.dtos.PaymentResponse;
import com.example.GasTuanDat.payment.dtos.PaymentUpdateRequest;
import com.example.GasTuanDat.payment.enitites.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "object", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "stock", ignore = true)
    PaymentEntity toEntity(PaymentCreateRequest request);

    @Mapping(target = "createdById", expression = "java(entity.getCreatedBy() != null ? entity.getCreatedBy().getEmployeeId() : null)")
    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "customerId", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null)")
    @Mapping(target = "supplierId", expression = "java(entity.getSupplier() != null ? entity.getSupplier().getSupplierId() : null)")
    @Mapping(target = "objectId", expression = "java(entity.getObject() != null ? entity.getObject().getObjectId() : null)")
    @Mapping(target = "transactionTypeId", expression = "java(entity.getTransactionType() != null ? entity.getTransactionType().getTransactionTypeId() : null)")
    @Mapping(target = "purchaseId", expression = "java(entity.getPurchase() != null ? entity.getPurchase().getPurchaseId() : null)")
    @Mapping(target = "stockId", expression = "java(entity.getStock() != null ? entity.getStock().getStockId() : null)")
    PaymentResponse toResponse(PaymentEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "paymentCode", source = "paymentCode")
    @Mapping(target = "paymentDate", source = "paymentDate")
    @Mapping(target = "paymentAmount", source = "paymentAmount")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "notes", source = "notes")
    void updateEntity(PaymentUpdateRequest request, @MappingTarget PaymentEntity entity);
}
