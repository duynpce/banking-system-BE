package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.GetAccountResponse;
import com.example.banking_system.account.service.domain.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<GetAccountResponse> get() {
        GetAccountResponse Response = accountService.get();
        return ResponseEntity.ok(Response);
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        accountService.delete();
        return ResponseEntity.ok("Account deleted successfully");
    }
}

