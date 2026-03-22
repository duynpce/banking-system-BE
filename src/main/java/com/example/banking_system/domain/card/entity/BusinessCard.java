package com.example.banking_system.domain.card.entity;

import com.example.banking_system.domain.card.constant.CardType;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "business_card_details")
@PrimaryKeyJoinColumn(name = "card_id")
@EqualsAndHashCode(callSuper = false)
public class BusinessCard extends CardDetails {


    public BusinessCard() {
        setCard(new Card());
    }

    public BusinessCard(String pinCode ,String cardNumber, String cardHolder, CardType type, CardPrivilege privilege) {
        setCard(new Card(pinCode,cardNumber, cardHolder,type, privilege));
    }
}
