package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.constant.LoanType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GetLoanResponse {
    private long id;
    private BigDecimal totalAmount;
    private BigDecimal leftAmount;
    private LocalDate dueDate;
    private LoanStatus status;
    private LoanType type;
    private LocalDate createdAt;
    private int durationMonths;
    private BigDecimal interestRate;
}

