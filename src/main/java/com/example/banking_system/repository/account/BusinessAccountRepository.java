package com.example.banking_system.repository.account;

import com.example.banking_system.entity.account.BusinessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessAccountRepository extends JpaRepository<BusinessAccount,Long> {

    boolean existsByOrganizationName(String organizationName);
    boolean existsByTaxIdNumber(String taxIdNumber);
}
