package com.example.banking_system.card.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "personal_card_details")
public class PersonalCard {

    @Id
    @Column(name = "card_id")
    private long cardId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private Card card;


    @Column(name = "reward_points", nullable = false)
    private int rewardPoint = 0;


    public PersonalCard() {

    }

    public PersonalCard(String cardNumber, CardType type, CardPrivilege privilege) {
        this.card = new Card(cardNumber, type, privilege);
    }
}
