package com.example.banking_system.entity.card;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.constant.CardType;
import com.example.banking_system.constant.CardPrivilege;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "personal_card")
@PrimaryKeyJoinColumn(name = "card_id", referencedColumnName = "id")
public class PersonalCard extends Card{

    public  PersonalCard() {super();}

    public PersonalCard(String cardNumber, CardType type, CardPrivilege cardPrivilege) {
        super(cardNumber, type, cardPrivilege);
    }

    @Override
    public AccountType getHolderType() {
        return AccountType.PERSONAL;
    }
}
