package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String number);

    boolean existsByNumber(String number);

    @Query(value = "select nextval('card_number_sequence')", nativeQuery = true)
    long getCardNumberSequence();

    Page<Card> findByAccount_Username(String username, Pageable pageable);
}
