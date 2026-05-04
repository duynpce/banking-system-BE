package com.example.banking_system.domain.card.repository;

import com.example.banking_system.domain.card.entity.PersonalCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalCardRepository extends JpaRepository<PersonalCard, Long> {

    @EntityGraph(attributePaths = {"card", "card.account"})
    List<PersonalCard> findByCard_Account_Id(long accountId);
}

