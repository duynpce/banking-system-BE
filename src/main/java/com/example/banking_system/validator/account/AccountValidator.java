package com.example.banking_system.validator.account;

import com.example.banking_system.dto.account.UpdateAccountRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final AccountService accountService;
    private final Util util;

    public void validateUniqueAccountDetails(Account account) {
        util.assertUnique(accountService.existsByUsername(account.getUsername()),"Username already exists");
        util.assertUnique(accountService.existsByPhoneNumber(account.getPhoneNumber()),"Phone number already exists");
        util.assertUnique(accountService.existsByEmail(account.getEmail()),"Email already exists");
    }

    // set only non-null fields from request to existingAccount after validating uniqueness
    public void setNonNullFieldsToUpdateAccount(UpdateAccountRequest request, Account existingAccount){
        if (request.getEmail() != null) {
            util.assertUnique(accountService.existsByEmail(request.getEmail()), "Email already exists");
            existingAccount.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null ) {
            util.assertUnique(accountService.existsByPhoneNumber(request.getPhoneNumber()), "Phone number already exists");
            existingAccount.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getAddress() != null){
            existingAccount.setAddress(request.getAddress());
        }
    }

}
