package com.example.banking_system.mapper;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.dto.account.CreateGovernmentAccountRequest;
import com.example.banking_system.dto.account.CreatePersonalAccountRequest;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    BusinessAccount toBusinessAccount(CreateBusinessAccountRequest createBusinessAccountRequest);
    PersonalAccount toPersonalAccount(CreatePersonalAccountRequest createPersonalAccountRequest);
    GovernmentAccount toGovernmentAccount(CreateGovernmentAccountRequest createGovernmentAccountRequest);
}
