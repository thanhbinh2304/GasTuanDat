package com.example.GasTuanDat.cashReceipt.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.cashReceipt.dtos.CashReceiptCreateRequest;
import com.example.GasTuanDat.cashReceipt.dtos.CashReceiptResponse;
import com.example.GasTuanDat.cashReceipt.dtos.CashReceiptUpdateRequest;
import com.example.GasTuanDat.cashReceipt.entities.CashReceiptEntity;

@Mapper(componentModel = "spring")
public interface CashReceiptMapper {

    @Mapping(target = "receiptId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "object", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    CashReceiptEntity toEntity(CashReceiptCreateRequest request);

    @Mapping(target = "createdById", expression = "java(entity.getCreatedBy() != null ? entity.getCreatedBy().getEmployeeId() : null)")
    @Mapping(target = "employeeId", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null)")
    @Mapping(target = "customerId", expression = "java(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null)")
    @Mapping(target = "supplierId", expression = "java(entity.getSupplier() != null ? entity.getSupplier().getSupplierId() : null)")
    @Mapping(target = "objectId", expression = "java(entity.getObject() != null ? entity.getObject().getObjectId() : null)")
    @Mapping(target = "transactionTypeId", expression = "java(entity.getTransactionType() != null ? entity.getTransactionType().getTransactionTypeId() : null)")
    @Mapping(target = "invoiceId", expression = "java(entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null)")
    CashReceiptResponse toResponse(CashReceiptEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "receiptCode", source = "receiptCode")
    @Mapping(target = "receiptDate", source = "receiptDate")
    @Mapping(target = "receiptAmount", source = "receiptAmount")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "createdDate", source = "createdDate")
    void updateEntity(CashReceiptUpdateRequest request, @MappingTarget CashReceiptEntity entity);
}
