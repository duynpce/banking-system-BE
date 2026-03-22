package com.example.banking_system.domain.account.service.domain;

import com.example.banking_system.domain.account.dto.GetAccountResponse;
import com.example.banking_system.domain.account.entity.Account;
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
    public void delete() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        accountQueryService.delete(account);
    }

    public String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = String.valueOf(secureRandom.nextLong(100000000000L)).formatted("%012d");
        } while (accountQueryService.existsByAccountNumber(accountNumber)); // Ensure uniqueness

        return accountNumber;
    }

}
