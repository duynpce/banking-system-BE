package com.example.banking_system.card.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.constant.CardPrivilege;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_card")
@PrimaryKeyJoinColumn(name = "card_id", referencedColumnName = "id")
public class BusinessCard extends Card {

    public BusinessCard () {super();}

    public BusinessCard(String cardNumber, CardType type, CardPrivilege cardPrivilege) {
        super(cardNumber, type, cardPrivilege);
    }

    @Override
    public AccountType getHolderType() {
        return AccountType.BUSINESS;
    }
}
