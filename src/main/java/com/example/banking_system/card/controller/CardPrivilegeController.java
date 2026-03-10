package com.example.banking_system.card.controller;

import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.service.domain.CardPrivilegeService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/card-privileges")
public class CardPrivilegeController {
    private final CardPrivilegeService cardPrivilegeService;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;

    public ResponseEntity<String> create(@Valid @RequestBody CreateCardPrivilegeRequest request){
        cardPrivilegeService.create(request);
        return ResponseEntity.ok("Card privilege created successfully");
    }

    @PutMapping
    public ResponseEntity<String> update(@Valid @RequestBody UpdateCardPrivilegeRequest request) {
        cardPrivilegeService.update(request);
        return ResponseEntity.ok("Card privilege updated successfully");
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCardPrivilegeAndIsActive(@PathVariable String code) {
        cardPrivilegeQueryService.deleteByPrivilegeCode(code);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardPrivilegeById(@PathVariable long id) {
        cardPrivilegeQueryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
