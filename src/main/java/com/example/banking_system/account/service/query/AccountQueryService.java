package com.example.banking_system.account.service.query;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.repository.AccountRepository;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService {
    private final AccountRepository accountRepository;

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return accountRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }
}
