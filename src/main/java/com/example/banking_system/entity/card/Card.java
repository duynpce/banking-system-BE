package com.example.banking_system.entity.card;

import com.example.banking_system.constant.*;
import com.example.banking_system.entity.account.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(name = "card_number", nullable = false, updatable = false,unique = true)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false,updatable = false)
    private LocalDate expirationDate;

    @Column(name = "annual_fee", nullable = false)
    private BigDecimal annualFee;

    @Column(name = "cashback_rate", nullable = false, columnDefinition = "numeric(5,2)")
    private BigDecimal cashbackRate;

    @Column(name ="type", nullable = false)
    private CardType type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "privileges", nullable = false)
    private CardPrivilege privilege;

    @Column(name = "status", nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    public Card(String cardNumber, CardType type, CardPrivilege privilege) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.privilege = privilege;
        this.cashbackRate = privilege.getCashbackRate(getHolderType(), type);
        this.expirationDate = privilege.getExpirationDate();
        this.annualFee = privilege.getAnnualFee(getHolderType());
    }

    public abstract  AccountType getHolderType();
}
