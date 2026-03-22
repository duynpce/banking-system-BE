package com.example.banking_system.domain.card.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table
(name = "card_privilege_code")
public class CardPrivilegeCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expiration_years", nullable = false)
    private int expirationYears;

    @Column(name = "spending_limit_daily", nullable = false, precision = 12, scale = 4)
    private BigDecimal spendingLimitDaily;

    @Column(name = "effective_from", nullable = false ,updatable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false, updatable = false)
    private LocalDate effectiveTo;

}
