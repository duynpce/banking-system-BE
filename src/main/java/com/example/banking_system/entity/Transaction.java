package com.example.banking_system.entity;


import com.example.banking_system.constant.TransactionStatus;
import com.example.banking_system.constant.TransactionType;
import com.example.banking_system.entity.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transferred_amount", nullable = false)
    private BigDecimal transferredAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @JoinColumn(name = "from_account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account fromAccount;

    @JoinColumn(name = "to_account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne
    private Account toAccount;
}
