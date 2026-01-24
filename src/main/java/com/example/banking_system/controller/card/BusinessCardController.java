package com.example.banking_system.controller.card;

import com.example.banking_system.dto.card.CreateBusinessCardRequest;
import com.example.banking_system.service.card.BusinessCardService;
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
    public ResponseEntity<String> create(@Valid @RequestBody CreateBusinessCardRequest request) {
        businessCardService.create(request);
        return ResponseEntity.ok("Business card created successfully");
    }
}
