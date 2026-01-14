package com.example.banking_system.service;

import com.example.banking_system.config.security.CustomUserDetailsService;
import com.example.banking_system.dto.auth.LoginRequest;
import com.example.banking_system.dto.auth.TokenResponse;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.exception.UnauthorizedException;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenResponse login(LoginRequest loginRequest) {
        Account account = accountService.findByUsername(loginRequest.getUsername());

        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())){
            throw new UnauthorizedException("Invalid password");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());

        return jwtUtil.generateTokens(userDetails); //bug here
    }
}
