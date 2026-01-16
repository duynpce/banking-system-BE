package com.example.banking_system.controller;

import com.example.banking_system.entity.account.Account;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Account> get() {
        String username = jwtUtil.getUsername();
        Account account = accountService.findByUsername(username);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        String username = jwtUtil.getUsername();
        accountService.delete(username);
        return ResponseEntity.ok("Account deleted successfully");
    }
}

