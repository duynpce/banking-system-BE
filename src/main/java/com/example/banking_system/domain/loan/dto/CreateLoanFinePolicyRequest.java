package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateLoanFinePolicyRequest {
    @NotNull(message = "fine type is required")
    private LoanFineType loanFineType;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "effective from is required")
    private LocalDate effectiveFrom;

    @NotNull(message = "effective to is required")
    private LocalDate effectiveTo;
}

