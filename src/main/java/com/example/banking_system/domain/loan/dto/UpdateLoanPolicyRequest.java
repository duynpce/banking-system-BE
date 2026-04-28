package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateLoanPolicyRequest {
    @NotNull(message = "id is required")
    private Long id;

    @Min(value = 1, message = "duration months must be greater than 0")
    private Integer durationMonths;

    @DecimalMin(value = "0.0", inclusive = false, message = "interest rate must be greater than 0")
    private Double interestRate;

    private LoanType loanType;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}

