package com.example.banking_system.controller.card;

import com.example.banking_system.dto.card.CreatePersonalCardRequest;
import com.example.banking_system.service.card.PersonalCardService;
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
    public ResponseEntity<String> create(@Valid @RequestBody CreatePersonalCardRequest request) {
        personalCardService.create(request);
        return ResponseEntity.ok("Personal card created successfully");
    }
}
