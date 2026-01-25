package com.example.banking_system.account.mapper;

import com.example.banking_system.account.dto.*;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.entity.PersonalAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    BusinessAccount toEntity(CreateBusinessAccountRequest createBusinessAccountRequest);
    PersonalAccount toEntity(CreatePersonalAccountRequest createPersonalAccountRequest);
    GovernmentAccount toEntity(CreateGovernmentAccountRequest createGovernmentAccountRequest);
    GetBusinessAccountResponse toDto(BusinessAccount businessAccount);
    GetPersonalAccountResponse toDto(PersonalAccount personalAccount);
    GetGovernmentAccountResponse toDto(GovernmentAccount governmentAccount);
}
