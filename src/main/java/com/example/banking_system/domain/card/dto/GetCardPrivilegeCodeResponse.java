package com.example.banking_system.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCardPrivilegeCodeResponse {
    private Long id;
    private String code;
    private Integer expirationYears;
    private BigDecimal spendingLimitDaily;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}

