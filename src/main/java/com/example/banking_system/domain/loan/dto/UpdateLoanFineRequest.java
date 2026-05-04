package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UpdateLoanFineRequest {
    @NotNull(message = "id is required")
    private Long id;

    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    private LoanFineType type;
}

