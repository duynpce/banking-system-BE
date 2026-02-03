package com.example.banking_system.card.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "business_card_details")
public class BusinessCard {

    @Id
    @Column(name = "card_id")
    private long cardId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private Card card;


    @Column(name = "department_code", nullable = false)
    private  String departmentCode;

    public BusinessCard() {

    }

    public BusinessCard(String cardNumber, CardType type, CardPrivilege privilege, String departmentCode) {
        this.card = new Card(cardNumber, type, privilege);
        this.departmentCode = departmentCode;
    }
}
