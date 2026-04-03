package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardPrivilegeCodeRepository extends JpaRepository<CardPrivilegeCode, Long> {

    boolean existsByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    boolean existsByCodeAndIdNotAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            Long excludeId,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    //find code that is effective on a specific date
    Optional<CardPrivilegeCode> findByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            String code,
            LocalDate from,
            LocalDate to
    );

    default Optional<CardPrivilegeCode> findByCodeAndDate(String code, LocalDate date) {
        return findByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                code, date, date
        );
    }

    default boolean hasOverlap(String code, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                code, effectiveTo, effectiveFrom
        );
    }

    default boolean hasOverlapExcludingId(String code, LocalDate effectiveFrom, LocalDate effectiveTo, Long excludeId) {
        return existsByCodeAndIdNotAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                code, excludeId, effectiveTo, effectiveFrom
        );
    }

    boolean existsByCode(String code);
}
