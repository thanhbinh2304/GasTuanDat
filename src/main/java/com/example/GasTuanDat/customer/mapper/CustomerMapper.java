package com.example.GasTuanDat.customer.mapper;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Component;

import com.example.GasTuanDat.customer.dtos.CustomerCreateRequest;
import com.example.GasTuanDat.customer.dtos.CustomerResponse;
import com.example.GasTuanDat.customer.dtos.CustomerUpdateRequest;
import com.example.GasTuanDat.customer.entities.CustomerEntity;

@Component
public class CustomerMapper {
    public CustomerEntity toEntity(CustomerCreateRequest request) {
        if (request == null) {
            return null;
        }

        return CustomerEntity.builder()
                .customerCode(request.getCustomerCode())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .note(request.getNote())
                .address(request.getAddress())
                .build();
    }

    public void updateEntity(CustomerUpdateRequest request, CustomerEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getFullName() != null) {
            entity.setFullName(request.getFullName());
        }

        if (request.getCustomerCode() != null) {
            entity.setCustomerCode(request.getCustomerCode());
        }

        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            entity.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getGender() != null) {
            entity.setGender(request.getGender());
        }

        if (request.getDateOfBirth() != null) {
            entity.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getNote() != null) {
            entity.setNote(request.getNote());
        }

        if (request.getAddress() != null) {
            entity.setAddress(request.getAddress());
        }
    }

    public CustomerResponse toResponse(CustomerEntity entity, OffsetDateTime lastTransactionDate) {
        if (entity == null) {
            return null;
        }

        return CustomerResponse.builder()
                .customerId(entity.getCustomerId())
                .customerCode(entity.getCustomerCode())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .gender(entity.getGender())
                .dateOfBirth(entity.getDateOfBirth())
                .note(entity.getNote())
                .address(entity.getAddress())
                .debt(entity.getDebt())
                .wardId(entity.getWard() != null ? entity.getWard().getWardId() : null)
                .wardName(entity.getWard() != null ? entity.getWard().getWardName() : null)
                .areaId((entity.getWard() != null && entity.getWard().getArea() != null) ? entity.getWard().getArea().getAreaId() : null)
                .areaName((entity.getWard() != null && entity.getWard().getArea() != null) ? entity.getWard().getArea().getAreaName() : null)
                .customerGroupId(
                        entity.getCustomerGroup() != null ? entity.getCustomerGroup().getCustomerGroupId() : null)
                .customerGroupName(entity.getCustomerGroup() != null ? entity.getCustomerGroup().getGroupName() : null)
                .lastTransactionDate(lastTransactionDate)
                .build();
    }
}
