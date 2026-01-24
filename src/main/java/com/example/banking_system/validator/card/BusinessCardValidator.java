package com.example.banking_system.validator.card;

import com.example.banking_system.entity.account.Account;
import com.example.banking_system.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessCardValidator {

    public void validateCreate(Account account) {
        // Check if account type can open business card
        if (!account.getType().canOpenBusinessCard()) {
            throw new ValidationException("This account type cannot open a business card");
        }
    }
}

