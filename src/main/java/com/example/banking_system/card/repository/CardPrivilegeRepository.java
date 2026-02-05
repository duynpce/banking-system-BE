package com.example.banking_system.card.repository;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.entity.CardPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPrivilegeRepository extends JpaRepository<CardPrivilege,String> {
    Optional<CardPrivilege> findByCardPrivilegeCode_CodeAndIsActiveTrue(String code);

    boolean existsByAccountTypeAndCardTypeAndIsActiveTrue(AccountType accountType, CardType cardType);
}
