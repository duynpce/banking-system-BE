package com.example.banking_system.entity.card;

import com.example.banking_system.constant.*;
import com.example.banking_system.entity.account.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "card")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Card {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate = LocalDate.now().plusYears(3);

    @Column(name = "annual_fee", nullable = false)
    private BigDecimal annualFee;

    @Column(name ="type", nullable = false)
    private CardType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "privileges", nullable = false)
    private Privilege privilege;

    @Column(name = "status", nullable = false)
    private CardStatus status = CardStatus.ACTIVE; // Default status is ACTIVE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    public Card(String cardNumber, BigDecimal annualFee
            , CardType type, Privilege privilege) {
        this.cardNumber = cardNumber;
        this.annualFee = annualFee;
        this.type = type;
        this.privilege = privilege;
    }

    public abstract  AccountType getHolderType();
}
