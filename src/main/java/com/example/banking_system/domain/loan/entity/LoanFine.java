package com.example.banking_system.domain.loan.entity;

import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.loan.constant.LoanFineStatus;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "loan_fine")
public class LoanFine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LoanFineType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanFineStatus status = LoanFineStatus.UNPAID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_fine_policy_id", referencedColumnName = "id", nullable = false, updatable = false)
    private LoanFinePolicy loanFinePolicy;
}
