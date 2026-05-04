package com.example.banking_system.domain.account.repository;

import com.example.banking_system.domain.account.entity.BusinessAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessAccountRepository extends JpaRepository<BusinessAccount,Long> {

    Optional<BusinessAccount> findByOrganizationName(String organizationName);
    Optional<BusinessAccount> findByTaxIdNumber(String taxIdNumber);

    @EntityGraph(attributePaths = {"account"})
    Optional<BusinessAccount> findByAccount_Username(String username);

    boolean existsByOrganizationName(String organizationName);
    boolean existsByTaxIdNumber(String taxIdNumber);
}
