package com.example.banking_system.auth;

import com.example.banking_system.config.security.CustomUserDetailsService;
import com.example.banking_system.auth.dto.LoginRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.account.service.AccountService;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public void login(LoginRequest loginRequest) {
        Account account = accountService.findByUsername(loginRequest.getUsername());

        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())){
            throw new UnauthorizedException("Invalid password");
        }

    }
}
