package com.example.banking_system.controller.account;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.dto.account.UpdateBusinessAccountRequest;
import com.example.banking_system.service.account.BusinessAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/business-accounts")
@RequiredArgsConstructor
public class BusinessAccountController {
    private final BusinessAccountService businessAccountService;

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


}
