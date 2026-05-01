package com.example.banking_system.domain.card.validator;

import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.common.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessCardValidator {

    public void validateCreate(Account account) {
        // Check if account type can open business card
        if (!account.getType().canOpenBusinessCard()) {
            throw new ForbiddenException("This account type cannot open a business card");
        }

        if (!account.getCreditRank().canOpenCard()) {
            throw new ForbiddenException("Account's credit rank does not permit opening a business card");
        }

        if(!account.getCreditRank().canOpenCard()){
            throw new ForbiddenException("Account's credit rank does not permit opening a business card");
        }
    }
}

