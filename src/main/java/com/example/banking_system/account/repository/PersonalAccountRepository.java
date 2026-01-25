package com.example.banking_system.account.repository;

import com.example.banking_system.account.entity.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {
    List<PersonalAccount> findByFullName(String fullName);
    Optional<PersonalAccount> findByIdCardNumber(String idCardNumber);

    boolean existsByFullName(String fullName);
    boolean existsByIdCardNumber(String idCardNumber);
}
