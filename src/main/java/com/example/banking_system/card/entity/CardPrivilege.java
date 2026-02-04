package com.example.banking_system.card.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(
        name = "card_privilege",
        uniqueConstraints = {
        @UniqueConstraint(
        name = "uk_card_privilege_acc_type_card_type",
        columnNames = { "for_account_type", "for_card_type" }
)
    })
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

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_code", referencedColumnName = "code", nullable = false)
    private CardPrivilegeCode cardPrivilegeCode;


    public String getPrivilegeCode() {
        return cardPrivilegeCode != null ? cardPrivilegeCode.getCode() : null;
    }

    public int getExpirationYears() {
        return cardPrivilegeCode != null ? cardPrivilegeCode.getExpirationYears() : 0;
    }

    public BigDecimal getSpendingLimitDaily() {
        return cardPrivilegeCode != null ? cardPrivilegeCode.getSpendingLimitDaily() : BigDecimal.ZERO;
    }

}
    