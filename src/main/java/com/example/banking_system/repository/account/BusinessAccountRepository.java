package com.example.banking_system.repository.account;

import com.example.banking_system.entity.account.BusinessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessAccountRepository extends JpaRepository<BusinessAccount,Long> {

    Optional<BusinessAccount> findByOrganizationName(String organizationName);
    boolean existsByOrganizationName(String organizationName);

    Optional<BusinessAccount> findByTaxIdNumber(String taxIdNumber);
    boolean existsByTaxIdNumber(String taxIdNumber);
}
