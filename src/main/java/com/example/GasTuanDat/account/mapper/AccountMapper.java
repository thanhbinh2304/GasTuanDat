package com.example.GasTuanDat.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.GasTuanDat.account.AccountEntity;
import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.account.dtos.AccountResponse;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    AccountEntity toAccount(AccountCreationRequest request);

    AccountResponse toAccountResponse(AccountEntity account);
}
