package com.example.banking_system.card.repository;

import com.example.banking_system.card.entity.CardPrivilegeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardPrivilegeCodeRepository extends JpaRepository<CardPrivilegeCode, String> {


}
