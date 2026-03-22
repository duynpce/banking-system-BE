package com.example.banking_system.domain.card.validator;

import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardPrivilegeValidator {
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final Util util;

    public void validateCreate(CardPrivilege cardPrivilege){
        util.assertUnique(
                cardPrivilegeQueryService.hasOverlap(cardPrivilege),
                "An active card privilege already exists for this account type and card type"
        );


    }

    public void validateUpdate(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // Set non-null fields to existing card privilege
        setNonNullFieldsToUpdateCardPrivilege(request, existingCardPrivilege);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeRequest request) {
        return request.getAnnualFee() == null && request.getCashBackRate() == null;
    }

    private void setNonNullFieldsToUpdateCardPrivilege(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (request.getAnnualFee() != null) {
            existingCardPrivilege.setAnnualFee(request.getAnnualFee());
        }

        if (request.getCashBackRate() != null) {
            existingCardPrivilege.setCashbackRate(request.getCashBackRate());
        }
    }
}
