package com.example.banking_system.domain.loan.controller;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanPolicyRequest;
import com.example.banking_system.domain.loan.service.domain.LoanPolicyService;
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
@RequestMapping("/v1/loan-policies")
public class LoanPolicyController {
    private final LoanPolicyService loanPolicyService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateLoanPolicyRequest request) {
        loanPolicyService.create(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan policy created successfully"));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateLoanPolicyRequest request) {
        loanPolicyService.update(request);
        return ResponseEntity.ok(ResponseDto.success(null, "Loan policy updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetLoanPolicyResponse>> getById(@PathVariable long id) {
        GetLoanPolicyResponse response = loanPolicyService.getById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan policy retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetLoanPolicyResponse>>> getByPage(
            @Valid @ModelAttribute PaginationDto paginationDto
    ) {
        List<GetLoanPolicyResponse> response = loanPolicyService.getByPage(paginationDto);
        return ResponseEntity.ok(ResponseDto.success(response, "Loan policies retrieved successfully"));
    }

    @GetMapping(params = {"loanType"})
    public ResponseEntity<ResponseDto<List<GetLoanPolicyResponse>>> getByLoanType(
            @Valid @ModelAttribute LoanType loanType
    ) {
        List<GetLoanPolicyResponse> response = loanPolicyService.getByLoanTypeAndIsActive(loanType);
        return ResponseEntity.ok(ResponseDto.success(response, "Active loan policies retrieved successfully"));
    }
}
