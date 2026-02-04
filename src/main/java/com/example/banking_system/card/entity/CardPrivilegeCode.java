package com.example.banking_system.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
(name = "card_privilege_code")
public class CardPrivilegeCode {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "expiration_years", nullable = false)
    private int expirationYears;
}
