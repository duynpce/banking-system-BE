package com.example.banking_system.card.repository;

import com.example.banking_system.card.entity.PersonalCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonalCardRepository extends JpaRepository<PersonalCard, Long> {

    @EntityGraph(attributePaths = {"card", "card.account"})
    List<PersonalCard> findByCard_Account_Id(long accountId);
}

