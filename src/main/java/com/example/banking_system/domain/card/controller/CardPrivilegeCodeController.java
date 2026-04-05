package com.example.banking_system.domain.card.controller;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeCodeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(params = {"!page", "!limit"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeCodeResponse>>> getAll() {
        List<GetCardPrivilegeCodeResponse> response = cardPrivilegeCodeService.getAll();
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege codes retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetCardPrivilegeCodeResponse>>> getByPage(
            @RequestParam int page,
            @RequestParam int limit
    ) {
        List<GetCardPrivilegeCodeResponse> response = cardPrivilegeCodeService.getByPage(page, limit);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege codes retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetCardPrivilegeCodeResponse>> getById(@PathVariable long id) {
        GetCardPrivilegeCodeResponse response = cardPrivilegeCodeService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege code retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ResponseDto<GetCardPrivilegeCodeResponse>> getByCodeAndIsActive(@PathVariable String code) {
        GetCardPrivilegeCodeResponse response = cardPrivilegeCodeService.getByCodeAndIsActive(code);
        return ResponseEntity.ok(ResponseDto.success(response, "Card privilege code retrieved successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateCardPrivilegeCodeRequest request) {
        cardPrivilegeCodeService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Card privilege code updated successfully"));
    }
}
