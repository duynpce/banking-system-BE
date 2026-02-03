package com.example.banking_system.auth;

import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.auth.dto.LoginRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountQueryService accountQueryService;

    public void login(LoginRequest loginRequest) {
        Account account = accountQueryService.findByUsername(loginRequest.getUsername());

        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())){
            throw new UnauthorizedException("Invalid password");
        }

    }
}
