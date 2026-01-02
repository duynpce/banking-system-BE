package com.example.banking_system.entity.card;

import com.example.banking_system.constant.CardHolderType;
import com.example.banking_system.entity.account.BusinessAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business_card")
@PrimaryKeyJoinColumn(name = "card_id", referencedColumnName = "id")
public class BusinessCard extends Card {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private BusinessAccount account;

    @Override
    public CardHolderType getHolderType() {
        return CardHolderType.BUSINESS;
    }
}
