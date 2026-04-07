package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CardPrivilegeRepository extends JpaRepository<CardPrivilege,Long> {


    Optional<CardPrivilege> findByCodeAndAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            AccountType accountType,
            CardType cardType,
            LocalDate from,
            LocalDate to
    );

    default Optional<CardPrivilege> findByCodeAndAccountTypeAndCardTypeAndDate(String privilegeCode, AccountType accountType, CardType cardType, LocalDate date) {
        return findByCodeAndAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                privilegeCode, accountType, cardType, date, date
        );
    }


    boolean existsByCodeAndAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            AccountType accountType,
            CardType cardType,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    default boolean hasOverlap(String code, AccountType accountType, CardType cardType, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByCodeAndAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                code, accountType, cardType, effectiveTo, effectiveFrom
        );
    }
}
