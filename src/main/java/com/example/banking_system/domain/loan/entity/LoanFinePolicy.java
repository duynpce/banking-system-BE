package com.example.banking_system.domain.loan.entity;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
public class LoanFinePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LoanFineType loanFineType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "effective_from",  nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to",   nullable = false)
    private LocalDate effectiveTo;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
