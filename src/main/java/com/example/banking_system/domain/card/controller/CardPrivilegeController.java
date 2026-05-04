package com.example.banking_system.domain.card.controller;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
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

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateCardPrivilegeRequest request) {
        cardPrivilegeService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege updated successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> getAll() {
        List<GetCardPrivilegeResponse> response = cardPrivilegeService.getAll();
        return ResponseEntity.ok(ResponseDto.success(response, "Card privileges retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> getById(@PathVariable long id) {
        GetCardPrivilegeResponse response = cardPrivilegeService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege retrieved successfully"));
    }

    // temp add meta data later
    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> getByPage(
            @RequestParam int page,
            @RequestParam int limit
    ) {
        List<GetCardPrivilegeResponse> response = cardPrivilegeService.getByPage(page, limit);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privileges retrieved successfully"));  
    }

    @GetMapping(params = {"accountType" , "cardType"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> getByAccountTypeAndCardTypeAndIsActive(
            @RequestParam AccountType accountType,
            @RequestParam CardType cardType
    ) {
        List<GetCardPrivilegeResponse> response = cardPrivilegeService.getByAccountTypeAndCardTypeAndIsActive(accountType, cardType);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privileges retrieved successfully"));
    }

    @GetMapping(params = {"code", "accountType", "cardType"})
    public ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> getByCodeAndAccountTypeAndCardTypeAndIsActive(
            @RequestParam String code,
            @RequestParam AccountType accountType,
            @RequestParam CardType cardType
    ) {
        GetCardPrivilegeResponse response = cardPrivilegeService.getByCodeAndAccountTypeAndCardTypeAndIsActive(code, accountType, cardType);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege retrieved successfully"));
    }

    @DeleteMapping(params = {"code", "accountType", "cardType"})
    public ResponseEntity<ResponseDto<String>> deleteCardPrivilegeAndIsActive(
            @RequestParam String code,
            @RequestParam AccountType accountType,
            @RequestParam CardType cardType
    ) {
        cardPrivilegeQueryService.deleteByPrivilegeCodeAndAccountTypeAndCardType(code, accountType, cardType);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege deleted by code"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteCardPrivilegeById(@PathVariable long id) {
        cardPrivilegeQueryService.deleteById(id);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege deleted by id"));
    }



}
