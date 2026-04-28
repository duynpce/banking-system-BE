package com.example.banking_system.domain.card.validator;

import com.example.banking_system.common.utility.ValidationUtil;
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
    private final ValidationUtil validationUtil;

    public void validateCreate(CardPrivilege cardPrivilege){
        validationUtil.validateEffectiveDateRange(cardPrivilege.getEffectiveFrom(), cardPrivilege.getEffectiveTo());

        util.assertUnique(
                cardPrivilegeQueryService.hasOverlap(cardPrivilege),
                "An active card privilege already exists for this account type and card type"
        );

    }


    public void validateUpdate(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        boolean updateActivePrivilege = !existingCardPrivilege.getEffectiveFrom().isAfter(LocalDate.now());

        if(updateActivePrivilege) {
            if(request.getEffectiveFrom() != null ) {
                throw new ValidationException("Effective from date cannot be updated for future card privilege");
            }
        }else{
            if(cardPrivilegeQueryService.hasOverlap(existingCardPrivilege)) {
                throw new ValidationException("An active card privilege already exists for this account type and card type with the given effective date range");
            }
        }

        LocalDate nextEffectiveFrom = request.getEffectiveFrom() != null
                ? request.getEffectiveFrom()
                : existingCardPrivilege.getEffectiveFrom();
        LocalDate nextEffectiveTo = request.getEffectiveTo() != null
                ? request.getEffectiveTo()
                : existingCardPrivilege.getEffectiveTo();

        validationUtil.validateEffectiveDateRange(nextEffectiveFrom, nextEffectiveTo);

        // Set non-null fields to existing card privilege
        setNonNullFieldsToUpdateCardPrivilege(request, existingCardPrivilege, updateActivePrivilege);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeRequest request) {
        return request.getAnnualFee() == null
                && request.getCashbackRate() == null
                && request.getExpirationYears() == null
                && request.getSpendingLimitDaily() == null
                && request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null;
    }

    private void setNonNullFieldsToUpdateCardPrivilege(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege, boolean updateActivePrivilege) {
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

        //if updating an active privilege --> not update effective from
        if (request.getEffectiveFrom() != null && updateActivePrivilege) {
            existingCardPrivilege.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            existingCardPrivilege.setEffectiveTo(request.getEffectiveTo());
        }
    }

}
