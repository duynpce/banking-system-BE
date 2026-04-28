package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateLoanFineRequest {
    @NotNull(message = "loan id is required")
    private Long loanId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "fine type is required")
    private LoanFineType type;

    @NotNull(message = "account id is required")
    @Min(value = 1, message = "account id must be a positive number")
    private Long accountId;

    @NotNull(message = "loan fine policy id is required")
    @Min(value = 1, message = "loan fine policy id must be a positive number")
    private Long loanFinePolicyId;
}

