package com.example.banking_system.domain.account.controller;

import com.example.banking_system.domain.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.domain.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.domain.account.service.domain.GovernmentAccountService;
import com.example.banking_system.common.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateGovernmentAccountRequest createGovernmentAccountRequest) {
        governmentAccountService.create(createGovernmentAccountRequest);
        return ResponseEntity.ok(ResponseDto.success(null, "Government account created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateGovernmentAccountRequest request) {
        governmentAccountService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Government account updated successfully"));
    }
}

