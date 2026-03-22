package com.example.banking_system.domain.card.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banking_system.domain.card.entity.BusinessCard;

import java.util.List;

// BusinessCard has no specific columns beyond Card, so no additional methods needed
public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
    @EntityGraph(attributePaths = {"card", "card.account"})
    List<BusinessCard> findByCard_Account_Id(long accountId);
}




