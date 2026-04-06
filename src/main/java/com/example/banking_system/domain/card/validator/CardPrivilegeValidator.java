package com.example.banking_system.domain.card.validator;

import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@RequiredArgsConstructor
public class CardPrivilegeValidator {
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final Util util;

    public void validateCreate(CardPrivilege cardPrivilege){
        validateEffectiveDateRange(cardPrivilege.getEffectiveFrom(), cardPrivilege.getEffectiveTo());

        util.assertUnique(
                cardPrivilegeQueryService.hasOverlap(cardPrivilege),
                "An active card privilege already exists for this account type and card type"
        );

        util.assertUnique(
                cardPrivilegeQueryService.hasCodeOverlap(
                        cardPrivilege.getCode(),
                        cardPrivilege.getEffectiveFrom(),
                        cardPrivilege.getEffectiveTo()
                ),
                "An overlapping card privilege code already exists"
        );
    }

    public void validateUpdate(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        LocalDate nextEffectiveFrom = request.getEffectiveFrom() != null
                ? request.getEffectiveFrom()
                : existingCardPrivilege.getEffectiveFrom();
        LocalDate nextEffectiveTo = request.getEffectiveTo() != null
                ? request.getEffectiveTo()
                : existingCardPrivilege.getEffectiveTo();

        validateEffectiveDateRange(nextEffectiveFrom, nextEffectiveTo);

        util.assertUnique(
                cardPrivilegeQueryService.hasCodeOverlapExcludingId(
                        existingCardPrivilege.getCode(),
                        nextEffectiveFrom,
                        nextEffectiveTo,
                        existingCardPrivilege.getId()
                ),
                "An overlapping card privilege code already exists"
        );

        // Set non-null fields to existing card privilege
        setNonNullFieldsToUpdateCardPrivilege(request, existingCardPrivilege);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeRequest request) {
        return request.getAnnualFee() == null
                && request.getCashbackRate() == null
                && request.getExpirationYears() == null
                && request.getSpendingLimitDaily() == null
                && request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null;
    }

    private void setNonNullFieldsToUpdateCardPrivilege(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (request.getAnnualFee() != null) {
            existingCardPrivilege.setAnnualFee(request.getAnnualFee());
        }

        if (request.getCashbackRate() != null) {
            existingCardPrivilege.setCashbackRate(request.getCashbackRate());
        }

        if (request.getExpirationYears() != null) {
            existingCardPrivilege.setExpirationYears(request.getExpirationYears());
        }

        if (request.getSpendingLimitDaily() != null) {
            existingCardPrivilege.setSpendingLimitDaily(request.getSpendingLimitDaily());
        }

        if (request.getEffectiveFrom() != null) {
            existingCardPrivilege.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            existingCardPrivilege.setEffectiveTo(request.getEffectiveTo());
        }
    }

    private void validateEffectiveDateRange(LocalDate effectiveFrom, LocalDate effectiveTo) {

        if (!effectiveTo.isAfter(effectiveFrom)) {
            throw new ValidationException("effective to date must be after effective from date");
        }
    }
}
