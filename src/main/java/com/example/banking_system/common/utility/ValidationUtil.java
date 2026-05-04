package com.example.banking_system.common.utility;

import com.example.banking_system.common.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidationUtil {
    public void validateEffectiveDateRange(LocalDate effectiveFrom, LocalDate effectiveTo) {
        if (!effectiveTo.isAfter(effectiveFrom)) {
            throw new ValidationException("effective to date must be after effective from date");
        }

        if (effectiveFrom.isBefore(LocalDate.now())) {
            throw new ValidationException("effective from date must be today or in the future");
        }
    }
}
