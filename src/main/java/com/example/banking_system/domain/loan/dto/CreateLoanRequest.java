package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateLoanRequest {
    @NotNull(message = "loan amount is required")
    @DecimalMin(value = "0.01", message = "loan amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "loan type is required")
    private LoanType type;

    @NotNull(message = "loan policy id is required")
    private Long policyId;
}

