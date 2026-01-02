package com.example.banking_system.entity.card;

import com.example.banking_system.constant.CardHolderType;
import com.example.banking_system.constant.CardType;
import com.example.banking_system.constant.Privilege;
import com.example.banking_system.entity.account.PersonalAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personal_card")
@PrimaryKeyJoinColumn(name = "card_id", referencedColumnName = "id")
public class PersonalCard extends Card{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private PersonalAccount account;

    public  PersonalCard() {super();}

    public PersonalCard(String cardNumber, Instant expirationDate, BigDecimal annualFee
            , CardType type, Privilege privilege, long personalAccountId) {
        super(cardNumber, expirationDate, annualFee, type, privilege);
    }

    @Override
    public CardHolderType getHolderType() {
        return CardHolderType.PERSONAL;
    }
}
