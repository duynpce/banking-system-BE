package com.example.banking_system.repository.card;

import com.example.banking_system.entity.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);

    boolean existsByCardNumber(String cardNumber);

    @Query(value = "select nextval('card_number_sequence')", nativeQuery = true)
    long getCardNumberSequence();
}

