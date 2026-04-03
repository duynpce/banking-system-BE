package com.example.banking_system.domain.card.dto;

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
public class CreateCardPrivilegeCodeRequest {
    @NotBlank(message = "Code must not be blank")
    private String code;

    @NotNull(message = "Expiration years must not be null")
    @Min(value = 1, message = "Expiration years must be at least 1")
    private Integer expirationYears;

    @NotNull(message = "Daily spending limit must not be null")
    @Min(value = 0, message = "Daily spending limit must be non-negative")
    private BigDecimal spendingLimitDaily;

    @NotNull(message = "Effective from date is required")
    @FutureOrPresent(message = "Effective from date must be today or in the future")
    private LocalDate effectiveFrom;

    @NotNull(message = "Effective to date is required")
    @FutureOrPresent(message = "Effective to date must be today or in the future")
    private LocalDate effectiveTo;
}
