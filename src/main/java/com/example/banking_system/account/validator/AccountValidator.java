package com.example.banking_system.account.validator;

import com.example.banking_system.account.dto.UpdateAccountRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.service.domain.AccountService;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final AccountQueryService accountQueryService;
    private final Util util;

    public void validateUniqueAccountDetails(Account account) {
        util.assertUnique(accountQueryService.existsByUsername(account.getUsername()),"Username already exists");
        util.assertUnique(accountQueryService.existsByPhoneNumber(account.getPhoneNumber()),"Phone number already exists");
        util.assertUnique(accountQueryService.existsByEmail(account.getEmail()),"Email already exists");
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
