package com.example.banking_system.card.dto;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
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
public class CreateCardPrivilegeRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotNull(message = "Base annual fee must not be null")
    private BigDecimal annualFee;

    @NotNull(message = "Cashback percentage must not be null")
    private BigDecimal cashbackRate;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotNull
    private LocalDate effectiveFrom;

    @NotNull
    private LocalDate effectiveTo;

    private boolean isActive;

}
