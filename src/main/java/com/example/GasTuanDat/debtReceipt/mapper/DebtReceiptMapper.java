package com.example.GasTuanDat.debtReceipt.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptCreateRequest;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptResponse;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptUpdateRequest;
import com.example.GasTuanDat.debtReceipt.entities.DebtReceiptEntity;

@Mapper(componentModel = "spring")
public interface DebtReceiptMapper {

    @Mapping(target = "receiptId", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "gasBook", ignore = true)
    @Mapping(target = "receiptCode", source = "code")
    @Mapping(target = "note", source = "notes")
    DebtReceiptEntity toEntity(DebtReceiptCreateRequest request);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "customerCode", ignore = true)
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "id", source = "receiptId")
    @Mapping(target = "code", source = "receiptCode")
    @Mapping(target = "notes", source = "note")
    DebtReceiptResponse toResponse(DebtReceiptEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "receiptId", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "gasBook", ignore = true)
    @Mapping(target = "receiptCode", source = "code")
    @Mapping(target = "note", source = "notes")
    void updateEntity(DebtReceiptUpdateRequest request, @MappingTarget DebtReceiptEntity entity);
}
