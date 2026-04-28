package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.service.domain.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loans")
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateLoanRequest request) {
        loanService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetLoanResponse>> getById(@PathVariable long id) {
        GetLoanResponse response = loanService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetLoanResponse>>> getByPage(
            @Valid @ModelAttribute PaginationDto paginationDto
    ) {
        List<GetLoanResponse> response = loanService.getByPage(paginationDto);
        return ResponseEntity.ok(ResponseDto.success(response, "Loans retrieved successfully"));
    }
}
