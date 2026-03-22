package com.example.banking_system.domain.account.validator;

import com.example.banking_system.domain.account.dto.UpdateAccountRequest;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.domain.AccountService;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final AccountQueryService accountQueryService;
    private final AccountService accountService;
    private final Util util;

    public void validateUniqueAccountDetails(Account account) {
        util.assertUnique(accountQueryService.existsByUsername(account.getUsername()),"Username already exists");
        util.assertUnique(accountQueryService.existsByPhoneNumber(account.getPhoneNumber()),"Phone number already exists");
        util.assertUnique(accountQueryService.existsByEmail(account.getEmail()),"Email already exists");
        util.assertUnique(accountQueryService.existsByAccountNumber(account.getAccountNumber()),"Account number already exists");
    }

    // set only non-null fields from request to existingAccount after validating uniqueness
    public void setNonNullFieldsToUpdateAccount(UpdateAccountRequest request, Account existingAccount){
        if (request.getEmail() != null) {
            util.assertUnique(accountQueryService.existsByEmail(request.getEmail()), "Email already exists");
            existingAccount.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null ) {
            util.assertUnique(accountQueryService.existsByPhoneNumber(request.getPhoneNumber()), "Phone number already exists");
            existingAccount.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getAddress() != null){
            existingAccount.setAddress(request.getAddress());
        }


    }

}
