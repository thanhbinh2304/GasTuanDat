package com.example.GasTuanDat.employee.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.employee.EmployeeEntity;
import com.example.GasTuanDat.employee.dtos.EmployeeCreateRequest;
import com.example.GasTuanDat.employee.dtos.EmployeeResponse;
import com.example.GasTuanDat.employee.dtos.EmployeeUpdateRequest;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "employeeId", ignore = true)
    EmployeeEntity toEntity(EmployeeCreateRequest request);

    EmployeeResponse toResponse(EmployeeEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "employeeCode", source = "employeeCode")
    @Mapping(target = "departmentId", source = "departmentId")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "hireDate", source = "hireDate")
    void updateEntity(EmployeeUpdateRequest request, @MappingTarget EmployeeEntity entity);
}