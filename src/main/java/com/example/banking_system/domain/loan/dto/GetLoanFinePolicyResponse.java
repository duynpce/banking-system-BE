package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetLoanFinePolicyResponse {
    private Long id;
    private LoanFineType type;
    private BigDecimal amount;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Instant createdAt;
}

