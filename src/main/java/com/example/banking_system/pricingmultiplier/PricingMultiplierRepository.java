package com.example.banking_system.pricingmultiplier;

import com.example.banking_system.account.constant.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingMultiplierRepository extends JpaRepository<PricingMultiplier, Long> {
    Optional<PricingMultiplier> findByAccountTypeAndMultiplierKind(AccountType accountType, String multiplierKind);
}
