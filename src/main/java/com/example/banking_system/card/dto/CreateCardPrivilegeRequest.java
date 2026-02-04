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
public class CreateCardPrivilegeRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotNull(message = "Base annual fee must not be null")
    private BigDecimal AnnualFee;

    @NotNull(message = "Cashback percentage must not be null")
    private BigDecimal CashBackRate;

}
