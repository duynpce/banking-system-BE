package com.example.banking_system.pricingmultiplier;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PricingMultiplierService {
    private final PricingMultiplierRepository pricingMultiplierRepository;

    public PricingMultiplier findByAccountTypeAndMultiplierKind(AccountType accountType, String multiplierKind) {
        return pricingMultiplierRepository.findByAccountTypeAndMultiplierKind(accountType, multiplierKind).orElseThrow(
                () -> new NotFoundException("Pricing multiplier not found for account type: " + accountType + " and multiplier kind: " + multiplierKind)
        );
    }
}
