package com.example.banking_system.validator;

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

    public void validateCreate(Account account) {
        validateUniqueAccountDetails(account);
    }

    private void validateUniqueAccountDetails(Account account) {
        util.assertNotConflictData(accountService.existsByUsername(account.getUsername()),"Username already exists");;;
        util.assertNotConflictData(accountService.existsByPhoneNumber(account.getPhoneNumber()),"Phone number already exists");
        util.assertNotConflictData(accountService.existsByEmail(account.getEmail()),"Email already exists");
    }
}
