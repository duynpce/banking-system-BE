package com.example.banking_system.account.controller;

import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.account.dto.UpdatePersonalAccountRequest;
import com.example.banking_system.account.service.domain.PersonalAccountService;
import com.example.banking_system.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/personal-accounts")
@RequiredArgsConstructor
public class PersonalAccountController {
    private final PersonalAccountService personalAccountService;
    private final PersonalAccountQueryService personalAccountQueryService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreatePersonalAccountRequest createPersonalAccountRequest) {
        personalAccountService.create(createPersonalAccountRequest);
        return ResponseEntity.ok(ResponseDto.success(null, "Personal account created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdatePersonalAccountRequest request) {
        personalAccountService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Personal account updated successfully"));
    }

    @GetMapping("/exists/id-card-number/{idCardNumber}")
    public ResponseEntity<ResponseDto<Boolean>> existsByIdCardNumber(@PathVariable(value = "idCardNumber") String idCardNumber) {
        boolean exists = personalAccountQueryService.existsByIdCardNumber(idCardNumber);
        return ResponseEntity.ok(ResponseDto.success(exists));
    }
}
