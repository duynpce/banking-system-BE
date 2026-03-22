package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CardPrivilegeRepository extends JpaRepository<CardPrivilege,Long> {

    Optional<CardPrivilege> findByCardPrivilegeCode_CodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            LocalDate from,
            LocalDate to
    );

    default Optional<CardPrivilege> findByPrivilegeCodeAndDate(String privilegeCode, LocalDate date) {
        return findByCardPrivilegeCode_CodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                privilegeCode, date, date
        );
    }

    boolean existsByAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            AccountType accountType,
            CardType cardType,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    default boolean hasOverlap(AccountType accountType, CardType cardType, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByAccountTypeAndCardTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                accountType, cardType, effectiveTo, effectiveFrom
        );
    }
}
