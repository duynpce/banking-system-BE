package com.example.banking_system.domain.card.validator;

import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

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

        // Set non-null fields to existing card privilege
        setNonNullFieldsToUpdateCardPrivilege(request, existingCardPrivilege);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeRequest request) {
        return request.getAnnualFee() == null
                && request.getCashbackRate() == null
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
