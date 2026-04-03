package com.example.banking_system.domain.card.controller;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/card-privilege-codes")
public class CardPrivilegeCodeController {

    private final CardPrivilegeCodeService cardPrivilegeCodeService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateCardPrivilegeCodeRequest request) {
        cardPrivilegeCodeService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege code created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateCardPrivilegeCodeRequest request) {
        cardPrivilegeCodeService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege code updated successfully"));
    }
}
