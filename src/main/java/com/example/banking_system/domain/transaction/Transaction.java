package com.example.banking_system.domain.transaction;


import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import com.example.banking_system.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "transferred_amount", nullable = false)
    private BigDecimal transferredAmount;

    @Column(name = "receiver_posted_balance")
    private BigDecimal receiverPostedBalance;

    @Column(name = "sender_posted_balance")
    private BigDecimal senderPostedBalance;

    @Column(name = "description",  nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "due_date")
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @JoinColumn(name = "sender_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account sender;

    @JoinColumn(name = "receiver_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account receiver;


    @PrePersist
    public void onCreate() {
        if(dueDate == null && type == TransactionType.PAYMENT) {
            dueDate = createdAt.plus(1, ChronoUnit.DAYS);
        }
    }
}
