package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetLoanPolicyResponse {
    private long id;
    private Integer durationMonths;
    private Double interestRate;
    private LoanType loanType;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Instant createdAt;
    private BigDecimal maxAmount;
}

