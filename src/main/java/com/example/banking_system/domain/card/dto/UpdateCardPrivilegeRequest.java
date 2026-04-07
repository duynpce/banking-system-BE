package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
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

    //not allowed to update code
    @Min(0)
    long id;

    @Min(value = 0, message = "Base annual fee must be non-negative")
    private BigDecimal annualFee;

    @Min(value = 0, message = "Cashback percentage must be non-negative")
    private BigDecimal cashbackRate;

    @Min(value = 1, message = "Expiration years must be at least 1")
    private Integer expirationYears;

    @Min(value = 0, message = "Daily spending limit must be non-negative")
    private BigDecimal spendingLimitDaily;

    @FutureOrPresent(message = "Effective from date must be today or in the future")
    private LocalDate effectiveFrom;

    @FutureOrPresent(message = "Effective to date must be today or in the future")
    private LocalDate effectiveTo;
}
