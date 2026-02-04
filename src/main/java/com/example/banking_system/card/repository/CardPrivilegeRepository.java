package com.example.banking_system.card.repository;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.entity.CardPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardPrivilegeRepository extends JpaRepository<CardPrivilege,String> {
    boolean existsByAccountTypeAndCardTypeAndIsActiveTrue(AccountType accountType, CardType cardType);

}
