package com.example.banking_system.account.service.domain;

import com.example.banking_system.account.dto.GetAccountResponse;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountQueryService accountQueryService;
    private final AccountMapper accountMapper;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public GetAccountResponse get() {
        String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsernameWithDetails(username);

        return accountMapper.toDto(account);
    }

    @Transactional
    public void delete() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        accountQueryService.delete(account);
    }

}
