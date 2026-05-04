package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.dto.CreateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFinePolicyResponse;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.service.domain.LoanFinePolicyService;
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
@RequestMapping("/v1/loan-fine-policies")
public class LoanFinePolicyController {
    private final LoanFinePolicyService loanFinePolicyService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateLoanFinePolicyRequest request) {
        loanFinePolicyService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan policy created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateLoanFinePolicyRequest request) {
        loanFinePolicyService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan policy updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetLoanFinePolicyResponse>> getById(@PathVariable long id) {
        GetLoanFinePolicyResponse response = loanFinePolicyService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan policy retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetLoanFinePolicyResponse>>> getByPage(
            @Valid @ModelAttribute PaginationDto paginationDto
    ) {
        List<GetLoanFinePolicyResponse> response = loanFinePolicyService.getByPage(paginationDto);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan policies retrieved successfully"));
    }

    @GetMapping(params = {"loanFineType"})
    public ResponseEntity<ResponseDto<List<GetLoanFinePolicyResponse>>> getByLoanFineType(
            @Valid @ModelAttribute LoanFineType loanFineType
    ) {
        List<GetLoanFinePolicyResponse> response = loanFinePolicyService.getByLoanFineTypeAndIsActive(loanFineType);
        return ResponseEntity.ok(ResponseDto.success(response, "Active loan policies retrieved successfully"));
    }
}
