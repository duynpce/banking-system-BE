package com.example.banking_system.mapper;

import com.example.banking_system.dto.account.*;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
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
