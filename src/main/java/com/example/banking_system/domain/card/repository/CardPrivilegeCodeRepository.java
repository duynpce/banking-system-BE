package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardPrivilegeCodeRepository extends JpaRepository<CardPrivilegeCode, Long> {
    @Query(value = """
    SELECT EXISTS(
        SELECT 1 FROM card_privilege_code
        WHERE code = :code
        AND daterange(effective_from, effective_to, '[]')
            && daterange(:from, :to, '[]')
    )
    """, nativeQuery = true)
    boolean existsByCodeAndDateRangeOverlap(
            @Param("code") String code,
            @Param("from") LocalDate effectiveFrom,
            @Param("to") LocalDate effectiveTo
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

    boolean existsByCode(String code);
}
