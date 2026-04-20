package com.example.banking_system.domain.account.service.domain;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.dto.GetAccountResponse;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.domain.account.entity.PersonalAccount;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountQueryService accountQueryService;
    private final AccountMapper accountMapper;
    private final SecureRandom secureRandom;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public GetAccountResponse get() {
        String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        return accountMapper.toDto(account);
    }

    @Transactional
    public String getNameByAccountNumber(String accountNumber) {
        Account account = accountQueryService.findByAccountNumber(accountNumber);

        switch (account.getType()) {
            case PERSONAL -> {
                return ((PersonalAccount) account.getAccountDetails()).getFullName();
            }
            case BUSINESS -> {
                return ((BusinessAccount) account.getAccountDetails()).getOrganizationName();
            }
            case GOVERNMENT -> {
                return ((GovernmentAccount) account.getAccountDetails()).getGovernmentDepartment();
            }
            default -> throw new ValidationException("Invalid account type");
        }
    }

    @Transactional
    public void delete() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        accountQueryService.delete(account);
    }

    public String generateAccountNumber() {
        String accountNumber;

        do {
            long randomNumber = secureRandom.nextLong(secureRandom.nextLong(1000000000000L));
            accountNumber = "%012d".formatted(randomNumber);
        } while (accountQueryService.existsByAccountNumber(accountNumber)); // Ensure uniqueness

        return accountNumber;
    }

}
