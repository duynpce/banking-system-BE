package com.example.banking_system.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardPrivilegeRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;

    private BigDecimal AnnualFee;
    private BigDecimal CashBackRate;
}
