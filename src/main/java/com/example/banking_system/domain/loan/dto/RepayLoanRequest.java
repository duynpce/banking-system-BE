package com.example.banking_system.domain.loan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepayLoanRequest {

    @NotNull(message = "loan id is required")
    @Min(value = 1, message = "loan id must be a positive integer")
    Long loanId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    BigDecimal amount;

}
