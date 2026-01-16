package com.example.banking_system.controller;

import com.example.banking_system.dto.account.CreateGovernmentAccountRequest;
import com.example.banking_system.dto.account.UpdateGovernmentAccountRequest;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.service.account.GovernmentAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/government-accounts")
@RequiredArgsConstructor
public class GovernmentAccountController {
    private final GovernmentAccountService governmentAccountService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateGovernmentAccountRequest createGovernmentAccountRequest) {
        governmentAccountService.create(createGovernmentAccountRequest);
        return ResponseEntity.ok("Government account created successfully");
    }

    @PutMapping
    public ResponseEntity<GovernmentAccount> update(@Valid @RequestBody UpdateGovernmentAccountRequest request) {
        GovernmentAccount updatedAccount = governmentAccountService.update(request);
        return ResponseEntity.ok(updatedAccount);
    }
}

