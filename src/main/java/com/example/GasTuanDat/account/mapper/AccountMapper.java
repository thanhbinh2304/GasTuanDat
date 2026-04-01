package com.example.GasTuanDat.account.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.account.AccountEntity;
import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.account.dtos.AccountCreationRequest;
import com.example.GasTuanDat.account.dtos.AccountResponse;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountEntity toAccount(AccountCreationRequest request);
    AccountResponse toAccountResponse(AccountEntity account);
    // void updateAccount(@MapperTarget AccountEntity account, )

}
