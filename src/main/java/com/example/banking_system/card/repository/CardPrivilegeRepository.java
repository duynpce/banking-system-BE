package com.example.banking_system.card.repository;

import com.example.banking_system.card.entity.CardPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPrivilegeRepository extends JpaRepository<CardPrivilege,String> {
    boolean existsByCode(String code);
}
