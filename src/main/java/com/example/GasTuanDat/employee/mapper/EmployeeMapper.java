package com.example.GasTuanDat.employee.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.GasTuanDat.employee.dtos.EmployeeCreateRequest;
import com.example.GasTuanDat.employee.dtos.EmployeeResponse;
import com.example.GasTuanDat.employee.dtos.EmployeeUpdateRequest;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "position", ignore = true)
    EmployeeEntity toEntity(EmployeeCreateRequest request);

    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "employeeCode", source = "employeeCode")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "positionId", source = "position.positionId")
    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "hireDate", source = "hireDate")
    @Mapping(target = "wardId", source = "ward.wardId")
    @Mapping(target = "wardName", source = "ward.wardName")
    @Mapping(target = "areaId", source = "ward.area.areaId")
    @Mapping(target = "areaName", source = "ward.area.areaName")
    EmployeeResponse toResponse(EmployeeEntity entity);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "hireDate", source = "hireDate")
    void updateEntity(EmployeeUpdateRequest request, @MappingTarget EmployeeEntity entity);
}
