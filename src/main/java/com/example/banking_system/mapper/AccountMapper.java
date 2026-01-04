package com.example.banking_system.mapper;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.entity.account.BusinessAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    BusinessAccount toBusinessAccount(CreateBusinessAccountRequest createBusinessAccountRequest);
}
