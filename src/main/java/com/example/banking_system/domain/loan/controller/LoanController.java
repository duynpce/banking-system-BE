package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.dto.LoanFilter;
import com.example.banking_system.domain.loan.service.domain.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetLoanResponse>>> getByFilter(
            @Valid @ModelAttribute LoanFilter loanFilter
    ) {
        List<GetLoanResponse> response = loanService.getByFilter(loanFilter);
        return ResponseEntity.ok(ResponseDto.success(response, "Loans retrieved successfully"));
    }

}
