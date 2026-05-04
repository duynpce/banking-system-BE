package com.example.banking_system.domain.card.entity;

import com.example.banking_system.domain.card.constant.CardStatus;
import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "card")
public class Card {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", nullable = false, updatable = false, unique = true)
    private String number;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDate expirationDate;

    @Column(name = "holder", nullable = false)
    private String holder;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CardType type;

    @Column(name = "balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "pin_code", nullable = false, length = 6)
    private String pinCode;

    @Column(name = "pin_code_attempts", nullable = false)
    private int pinCodeAttempts = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_privilege_id", referencedColumnName = "id", nullable = false, updatable = false)
    private CardPrivilege privilege;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    //eager mapping because we always need card details when we have a card
    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private CardDetails cardDetails;

    public Card(){
    }

    public Card(String pinCode, String number, String holder, CardType type, CardPrivilege privilege) {
        this.pinCode = pinCode;
        this.number = number;
        this.holder = holder;
        this.type = type;
        this.privilege = privilege;
    }



}
