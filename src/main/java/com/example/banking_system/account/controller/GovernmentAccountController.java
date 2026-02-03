package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.account.service.domain.GovernmentAccountService;
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
    public ResponseEntity<String> update(@Valid @RequestBody UpdateGovernmentAccountRequest request) {
        governmentAccountService.update(request);
        return ResponseEntity.ok("Government account updated successfully");
    }
}

