package com.example.banking_system.controller;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;

import com.example.banking_system.service.account.BusinessAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/business-accounts")
@RequiredArgsConstructor
public class BusinessAccountController {
    private final BusinessAccountService businessAccountService;

    @PostMapping()
    public ResponseEntity<String> create(@Valid @RequestBody CreateBusinessAccountRequest createBusinessAccountRequest) {
        businessAccountService.create(createBusinessAccountRequest);
        return ResponseEntity.ok("Business account created successfully");
    }

    @GetMapping("/test")
    public String test() {
        return "Business Account Controller is working";
    }
}
