package com.example.banking_system.card.controller;

import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.service.domain.PersonalCardService;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/personal-cards")
@RequiredArgsConstructor
public class PersonalCardController {
    private final PersonalCardService personalCardService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreatePersonalCardRequest request) {
        personalCardService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Personal card created successfully"));
    }
}
