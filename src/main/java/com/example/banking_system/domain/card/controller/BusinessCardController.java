package com.example.banking_system.domain.card.controller;

import com.example.banking_system.domain.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.domain.card.service.domain.BusinessCardService;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/business-cards")
@RequiredArgsConstructor
public class BusinessCardController {
    private final BusinessCardService businessCardService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateBusinessCardRequest request) {
        businessCardService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Business card created successfully"));
    }
}
