package com.example.banking_system.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "card_privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPrivilege {

    @Column(name = "code", nullable = false, unique = true)
    @Id
    String code;

    @Column(name = "base_annual_fee", nullable = false, precision = 12, scale = 2)
    BigDecimal baseAnnualFee;

    @Column(name = "base_expiration_years", nullable = false)
    int baseExpirationYears;

    @Column(name = "base_cashback_rate", nullable = false, precision = 12, scale = 2)
    BigDecimal baseCashbackRate;

}
