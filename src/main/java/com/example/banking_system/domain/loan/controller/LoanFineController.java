package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.dto.CreateLoanFineRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFineResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFineRequest;
import com.example.banking_system.domain.loan.service.domain.LoanFineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loan-fines")
public class LoanFineController {
    private final LoanFineService loanFineService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateLoanFineRequest request) {
        loanFineService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan fine created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateLoanFineRequest request) {
        loanFineService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan fine updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetLoanFineResponse>> getById(@PathVariable long id) {
        GetLoanFineResponse response = loanFineService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan fine retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetLoanFineResponse>>> getByPage(
            @Valid @ModelAttribute PaginationDto paginationDto
    ) {
        List<GetLoanFineResponse> response = loanFineService.getByPage(paginationDto);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan fines retrieved successfully"));
    }
}
