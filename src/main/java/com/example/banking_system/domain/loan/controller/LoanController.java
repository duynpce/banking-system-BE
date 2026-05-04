package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.*;
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

    @PostMapping("/repay")
    public ResponseEntity<ResponseDto<String>> repay(@Valid @RequestBody RepayLoanRequest request) {
        loanService.repayLoan(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan repayment successful"));
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
        ResponseDto<List<GetLoanResponse>> response = loanService.getByFilter(loanFilter);;
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports")
    public ResponseEntity<ResponseDto<GetLoanReportResponse>> getByReports(
            //null status mean query all kind of status
            @RequestParam(required = false) LoanStatus loanStatus) {
        GetLoanReportResponse response = loanService.getByReports(loanStatus);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan report retrieved successfully"));

    }
}
