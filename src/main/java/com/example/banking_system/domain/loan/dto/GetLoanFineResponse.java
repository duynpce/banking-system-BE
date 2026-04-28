package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetLoanFineResponse {
    private long id;
    private long loanId;
    private BigDecimal amount;
    private LocalDate createdAt;
    private LoanFineType type;
}

