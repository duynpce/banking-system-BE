package com.example.banking_system.account.repository;

import com.example.banking_system.account.entity.BusinessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessAccountRepository extends JpaRepository<BusinessAccount,Long> {

    Optional<BusinessAccount> findByOrganizationName(String organizationName);
    Optional<BusinessAccount> findByTaxIdNumber(String taxIdNumber);

    boolean existsByOrganizationName(String organizationName);
    boolean existsByTaxIdNumber(String taxIdNumber);
}
