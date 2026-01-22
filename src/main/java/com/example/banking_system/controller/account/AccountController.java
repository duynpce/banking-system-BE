package com.example.banking_system.controller.account;

import com.example.banking_system.dto.account.GetAccountResponse;
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
    public ResponseEntity<GetAccountResponse> get() {
        String username = jwtUtil.getUsername();
        GetAccountResponse Response = accountService.getByUsername(username);
        return ResponseEntity.ok(Response);
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        String username = jwtUtil.getUsername();
        accountService.deleteByUsername(username);
        return ResponseEntity.ok("Account deleted successfully");
    }
}

