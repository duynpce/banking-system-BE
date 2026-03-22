package com.example.banking_system.domain.pricingmultiplier;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pricing_multiplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingMultiplier {

    @Column(name ="id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "multiplier_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal multiplierValue;

    @Column(name = "multiplier_kind", nullable = false)
    private String multiplierKind;


}
