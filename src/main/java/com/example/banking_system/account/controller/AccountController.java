package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.GetAccountResponse;
import com.example.banking_system.account.service.domain.AccountService;
import com.example.banking_system.account.service.query.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountQueryService accountQueryService;

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

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable(value = "username") String username) {
        boolean exists = accountQueryService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable(value = "email") String email) {
        boolean exists = accountQueryService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/phone-number/{phoneNumber}")
    public ResponseEntity<Boolean> existsByPhoneNumber(@PathVariable(value = "phoneNumber") String phoneNumber) {
        boolean exists = accountQueryService.existsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(exists);
    }
}
