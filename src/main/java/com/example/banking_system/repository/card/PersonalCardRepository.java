package com.example.banking_system.repository.card;

import com.example.banking_system.entity.card.PersonalCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalCardRepository extends JpaRepository<PersonalCard, Long> {
    // PersonalCard has no specific columns beyond Card, so no additional methods needed
}

