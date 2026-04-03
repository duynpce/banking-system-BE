package com.example.banking_system.domain.card.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Min(value = 0, message = "Base annual fee must be non-negative")
    private BigDecimal annualFee;

    @Min(value = 0, message = "Cashback percentage must be non-negative")
    private BigDecimal cashbackRate;

    @FutureOrPresent(message = "Effective from date must be today or in the future")
    private LocalDate effectiveFrom;

    @FutureOrPresent(message = "Effective to date must be today or in the future")
    private LocalDate effectiveTo;
}
