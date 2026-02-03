package com.example.banking_system.account.repository;

import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.entity.PersonalAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {
    List<PersonalAccount> findByFullName(String fullName);
    Optional<PersonalAccount> findByIdCardNumber(String idCardNumber);

    @EntityGraph(attributePaths = {"account"})
    Optional<PersonalAccount> findByAccount_Username(String username);

    boolean existsByFullName(String fullName);
    boolean existsByIdCardNumber(String idCardNumber);
}
