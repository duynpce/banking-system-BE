package com.example.banking_system.repository.card;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banking_system.entity.card.BusinessCard;

    // BusinessCard has no specific columns beyond Card, so no additional methods needed
public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {

    }




