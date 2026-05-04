package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateLoanPolicyRequest {
    @NotNull(message = "duration months is required")
    @Min(value = 1, message = "duration months must be greater than 0")
    private Integer durationMonths;

    @NotNull(message = "interest rate is required")
    @DecimalMin(value = "0.01", message = "interest rate must be greater than 0")
    private BigDecimal interestRate;

    @DecimalMin(value = "0.01", message = "max amount must be greater than 0")
    private BigDecimal maxAmount;

    @NotNull(message = "loan type is required")
    private LoanType loanType;

    @NotNull(message = "effective from is required")
    private LocalDate effectiveFrom;

    @NotNull(message = "effective to is required")
    private LocalDate effectiveTo;
}

