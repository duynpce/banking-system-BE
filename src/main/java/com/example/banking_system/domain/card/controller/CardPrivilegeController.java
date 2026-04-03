package com.example.banking_system.domain.card.controller;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.dto.ResponseDto;
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

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateCardPrivilegeRequest request){
        cardPrivilegeService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateCardPrivilegeRequest request) {
        cardPrivilegeService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege updated successfully"));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ResponseDto<String>> deleteCardPrivilegeAndIsActive(@PathVariable String code) {
        cardPrivilegeQueryService.deleteByPrivilegeCode(code);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege deleted by code"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteCardPrivilegeById(@PathVariable long id) {
        cardPrivilegeQueryService.deleteById(id);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege deleted by id"));
    }



}
