package com.example.banking_system.entity.card;

import com.example.banking_system.constant.CardHolderType;
import com.example.banking_system.constant.CardStatus;
import com.example.banking_system.constant.CardType;
import com.example.banking_system.constant.Privilege;
import com.example.banking_system.entity.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

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
    private Instant expirationDate;

    @Column(name = "annual_fee", nullable = false)
    private BigDecimal annualFee;

    @Column(name ="type", nullable = false)
    private CardType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "privileges", nullable = false)
    private Privilege privilege;

    @Column(name = "status", nullable = false)
    private CardStatus status;

    public Card(String cardNumber, Instant expirationDate, BigDecimal annualFee
            , CardType type, Privilege privilege) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.annualFee = annualFee;
        this.type = type;
        this.privilege = privilege;
    }

    public abstract CardHolderType getHolderType();
}
