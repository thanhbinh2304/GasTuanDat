package com.example.GasTuanDat.supplier.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.supplier.dtos.SupplierCreateRequest;
import com.example.GasTuanDat.supplier.dtos.SupplierResponse;
import com.example.GasTuanDat.supplier.dtos.SupplierUpdateRequest;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    // Map CreateRequest to Entity (ignore id and timestamps)
    @Mapping(target = "supplierId", ignore = true)
    SupplierEntity toEntity(SupplierCreateRequest request);

    // Map Entity to Response
    SupplierResponse toResponse(SupplierEntity entity);

    // Update Entity from UpdateRequest (only update specified fields)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "wardId", source = "wardId")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "address", source = "address")
    void updateEntity(SupplierUpdateRequest request, @MappingTarget SupplierEntity entity);
}
