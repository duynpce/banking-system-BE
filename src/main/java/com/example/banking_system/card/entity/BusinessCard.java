package com.example.banking_system.card.entity;

import com.example.banking_system.card.constant.CardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "business_card_details")
@PrimaryKeyJoinColumn(name = "card_id")
@EqualsAndHashCode(callSuper = false)
public class BusinessCard extends CardDetails {

    @Column(name = "department_code", nullable = false)
    private String departmentCode;

    @Column(name = "expense_limit", precision = 12, scale = 2)
    private BigDecimal expenseLimit;

    public BusinessCard() {
        setCard(new Card());
    }

    public BusinessCard(String cardNumber, CardType type, CardPrivilege privilege, String departmentCode) {
        setCard(new Card(cardNumber, type, privilege));
        this.departmentCode = departmentCode;
    }
}
