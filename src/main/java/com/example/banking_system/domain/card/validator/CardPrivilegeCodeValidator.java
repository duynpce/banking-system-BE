package com.example.banking_system.domain.card.validator;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CardPrivilegeCodeValidator {

    private final CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;
    private final Util util;

    public void validateCreate(CardPrivilegeCode cardPrivilegeCode) {
        validateEffectiveDateRange(cardPrivilegeCode.getEffectiveFrom(), cardPrivilegeCode.getEffectiveTo());

        util.assertUnique(
                cardPrivilegeCodeQueryService.hasOverlap(
                        cardPrivilegeCode.getCode(),
                        cardPrivilegeCode.getEffectiveFrom(),
                        cardPrivilegeCode.getEffectiveTo()
                ),
                "An overlapping card privilege code already exists"
        );
    }

    public void validateUpdate(UpdateCardPrivilegeCodeRequest request, CardPrivilegeCode existingCardPrivilegeCode) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        LocalDate nextEffectiveFrom = request.getEffectiveFrom() != null
                ? request.getEffectiveFrom()
                : existingCardPrivilegeCode.getEffectiveFrom();
        LocalDate nextEffectiveTo = request.getEffectiveTo() != null
                ? request.getEffectiveTo()
                : existingCardPrivilegeCode.getEffectiveTo();

        validateEffectiveDateRange(nextEffectiveFrom, nextEffectiveTo);

        util.assertUnique(
                cardPrivilegeCodeQueryService.hasOverlapExcludingId(
                        existingCardPrivilegeCode.getCode(),
                        nextEffectiveFrom,
                        nextEffectiveTo,
                        existingCardPrivilegeCode.getId()
                ),
                "An overlapping card privilege code already exists"
        );

        setNonNullFieldsToUpdateCardPrivilegeCode(request, existingCardPrivilegeCode);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeCodeRequest request) {
        return request.getExpirationYears() == null
                && request.getSpendingLimitDaily() == null
                && request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null;
    }

    private void setNonNullFieldsToUpdateCardPrivilegeCode(UpdateCardPrivilegeCodeRequest request, CardPrivilegeCode existingCardPrivilegeCode) {
        if (request.getExpirationYears() != null) {
            existingCardPrivilegeCode.setExpirationYears(request.getExpirationYears());
        }

        if (request.getSpendingLimitDaily() != null) {
            existingCardPrivilegeCode.setSpendingLimitDaily(request.getSpendingLimitDaily());
        }

        if (request.getEffectiveFrom() != null) {
            existingCardPrivilegeCode.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            existingCardPrivilegeCode.setEffectiveTo(request.getEffectiveTo());
        }
    }

    private void validateEffectiveDateRange(LocalDate effectiveFrom, LocalDate effectiveTo) {
        if (!effectiveTo.isAfter(effectiveFrom)) {
            throw new ValidationException("effective to date must be after effective from date");
        }
    }
}
