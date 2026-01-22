package com.example.banking_system.entity.card;

import com.example.banking_system.constant.AccountType;
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

    private String ownerName;

    public  PersonalCard() {super();}

    public PersonalCard(String cardNumber, BigDecimal annualFee
            , CardType type, Privilege privilege, long personalAccountId) {
        super(cardNumber, annualFee, type, privilege);
    }

    @Override
    public AccountType getHolderType() {
        return AccountType.PERSONAL;
    }
}
