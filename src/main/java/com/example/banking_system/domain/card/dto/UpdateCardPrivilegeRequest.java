package com.example.banking_system.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardPrivilegeRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;


    private BigDecimal AnnualFee;
    private BigDecimal CashBackRate;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
