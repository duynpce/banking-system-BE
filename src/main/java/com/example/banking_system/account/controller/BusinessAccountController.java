package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.account.service.domain.BusinessAccountService;
import com.example.banking_system.account.service.query.BusinessAccountQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/business-accounts")
@RequiredArgsConstructor
public class BusinessAccountController {
    private final BusinessAccountService businessAccountService;
    private final BusinessAccountQueryService businessAccountQueryService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateBusinessAccountRequest createBusinessAccountRequest) {
        businessAccountService.create(createBusinessAccountRequest);
        return ResponseEntity.ok("Business account created successfully");
    }

    @PutMapping
    public ResponseEntity<String> update(@Valid @RequestBody UpdateBusinessAccountRequest request) {
         businessAccountService.update(request);
        return ResponseEntity.ok("Business account updated successfully");
    }

    @GetMapping("/exists/tax-id-number/{taxIdNumber}")
    public ResponseEntity<Boolean> existsByTaxIdNumber(@PathVariable(value = "taxIdNumber") String taxIdNumber) {
        boolean exists = businessAccountQueryService.existsByTaxIdNumber(taxIdNumber);
        return ResponseEntity.ok(exists);
    }
}
