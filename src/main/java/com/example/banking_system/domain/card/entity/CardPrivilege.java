package com.example.banking_system.domain.card.entity;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "card_privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPrivilege {

    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "for_account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "for_card_type", nullable = false)
    private CardType cardType;

    @Column(name = "annual_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal annualFee;

    @Column(name = "cashback_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal cashbackRate;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expiration_years", nullable = false)
    private int expirationYears;

    @Column(name = "spending_limit_daily", nullable = false, precision = 12, scale = 4)
    private BigDecimal spendingLimitDaily;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    public String getPrivilegeCode() {
        return code;
    }

}
