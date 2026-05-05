package com.example.banking_system.domain.loan.entity;

import com.example.banking_system.domain.loan.constant.LoanType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "loan_policy")
@Data
@NoArgsConstructor
public class LoanPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "loan_type" , nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Column(name = "effective_from",  nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to",   nullable = false)
    private LocalDate effectiveTo;

    @Column(name = "max_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal maxAmount;

    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();



}
