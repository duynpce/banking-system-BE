package com.example.banking_system.controller.account;

import com.example.banking_system.dto.account.CreatePersonalAccountRequest;
import com.example.banking_system.dto.account.UpdatePersonalAccountRequest;
import com.example.banking_system.service.account.PersonalAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/personal-accounts")
@RequiredArgsConstructor
public class PersonalAccountController {
    private final PersonalAccountService personalAccountService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreatePersonalAccountRequest createPersonalAccountRequest) {
        personalAccountService.create(createPersonalAccountRequest);
        return ResponseEntity.ok("Personal account created successfully");
    }

    @PutMapping
    public ResponseEntity<String> update(@Valid @RequestBody UpdatePersonalAccountRequest request) {
        personalAccountService.update(request);
        return ResponseEntity.ok("Personal account updated successfully");
    }
}

