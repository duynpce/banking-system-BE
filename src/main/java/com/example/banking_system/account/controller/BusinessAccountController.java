package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.account.service.domain.BusinessAccountService;
import com.example.banking_system.account.service.query.BusinessAccountQueryService;
import com.example.banking_system.common.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateBusinessAccountRequest createBusinessAccountRequest) {
        businessAccountService.create(createBusinessAccountRequest);
        return ResponseEntity.ok(ResponseDto.success(null, "Business account created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateBusinessAccountRequest request) {
         businessAccountService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Business account updated successfully"));
    }

    @GetMapping("/exists/tax-id-number/{taxIdNumber}")
    public ResponseEntity<ResponseDto<Boolean>> existsByTaxIdNumber(@PathVariable(value = "taxIdNumber") String taxIdNumber) {
        boolean exists = businessAccountQueryService.existsByTaxIdNumber(taxIdNumber);
        return ResponseEntity.ok(ResponseDto.success(exists));
    }
}
