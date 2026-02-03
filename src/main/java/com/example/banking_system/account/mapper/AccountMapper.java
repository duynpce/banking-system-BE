// Java
package com.example.banking_system.account.mapper;

import com.example.banking_system.account.dto.*;
import com.example.banking_system.account.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @SubclassMapping(source = PersonalAccount.class,   target = GetPersonalAccountResponse.class)
    @SubclassMapping(source = BusinessAccount.class,   target = GetBusinessAccountResponse.class)
    @SubclassMapping(source = GovernmentAccount.class, target = GetGovernmentAccountResponse.class)
    @Mapping(target = "email",       source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "address",     source = "account.address")
    @Mapping(target = "type",        source = "account.type")
    @Mapping(target = "status",      source = "account.status")
    GetAccountResponse toDto(AccountDetails details);

    // Dispatcher that returns a subclass instance (typed as parent)
    default GetAccountResponse toDto(Account account) {
        if (account == null) {
            return null;
        }
        return toDto(account.getAccountDetails());
    }

    // ToEntity mappings: compose nested Account
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "account",   source = "request")
    PersonalAccount toEntity(CreatePersonalAccountRequest request);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "account",   source = "request")
    BusinessAccount toEntity(CreateBusinessAccountRequest request);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "account",   source = "request")
    GovernmentAccount toEntity(CreateGovernmentAccountRequest request);

    // Helper used for the nested mapping above; MapStruct will pick it automatically
    Account toAccount(CreateAccountRequest source);
}
