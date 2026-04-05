package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCardPrivilegeResponse {
    private Long id;
    private String privilegeCode;
    private AccountType accountType;
    private CardType cardType;
    private BigDecimal annualFee;
    private BigDecimal cashbackRate;
    private Integer expirationYears;
    private BigDecimal spendingLimitDaily;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}

