package com.example.banking_system.domain.card.controller;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(params = {"!page", "!limit"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> getAll() {
        List<GetCardPrivilegeResponse> response = cardPrivilegeService.getAll();
        return ResponseEntity.ok(ResponseDto.success(response, "Card privileges retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> getByPage(
            @RequestParam int page,
            @RequestParam int limit
    ) {
        List<GetCardPrivilegeResponse> response = cardPrivilegeService.getByPage(page, limit);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privileges retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> getById(@PathVariable long id) {
        GetCardPrivilegeResponse response = cardPrivilegeService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> getByCodeAndIsActive(@PathVariable String code) {
        GetCardPrivilegeResponse response = cardPrivilegeService.getByCodeAndIsActive(code);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege retrieved successfully"));
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
