package com.example.banking_system.card.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banking_system.card.entity.BusinessCard;

    // BusinessCard has no specific columns beyond Card, so no additional methods needed
public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {

    }




