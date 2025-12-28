package com.example.banking_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transferred_amount", nullable = false)
    private long transferredAmount;

    @Column(name = "from_account_id", nullable = false)
    private long fromAccountId;

    @Column(name = "to_account_id", nullable = false)
    private long toAccountId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private String type;

    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)// optional = false similar to nullable = false but for backend not database
    private Account account;
}
