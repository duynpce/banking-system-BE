package com.example.banking_system.domain.loan.entity;

import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq_id")
    @SequenceGenerator(name = "loan_seq_id", sequenceName = "loan_seq_id", allocationSize = 1)
    private long id;

    @Column(name = "total_amount", nullable = false, updatable = false)
    private BigDecimal totalAmount;

    @Column(name = "base_amount", nullable = false, updatable = false)
    private BigDecimal baseAmount;

    @Column(name ="left_amount", nullable = false)
    private BigDecimal leftAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus status = LoanStatus.CURRENT_PAYMENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LoanType type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_policy_id", referencedColumnName = "id", nullable = false, updatable = false)
    private  LoanPolicy policy;

    @PrePersist
    public void prePersist() {
        // set leftAmount to totalAmount when creating a new loan
        this.leftAmount = this.totalAmount;;
    }


}
