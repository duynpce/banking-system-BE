package com.example.banking_system.card.validator;

import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.repository.CardPrivilegeRepository;
import com.example.banking_system.card.service.CardPrivilegeService;
import com.example.banking_system.common.exception.ConflictDataException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardPrivilegeValidator {
    private final CardPrivilegeRepository cardPrivilegeRepository ;
    private final Util util;

    public void validateCreate(CardPrivilege cardPrivilege){
        util.assertUnique(cardPrivilegeRepository.existsByCode(cardPrivilege.getCode()),
                "Card privilege with code " + cardPrivilege.getCode() + " already exists");
    }

    public void validateUpdate(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // Set non-null fields to existing card privilege
        setNonNullFieldsToUpdateCardPrivilege(request, existingCardPrivilege);
    }

    private boolean isAllFieldsNull(UpdateCardPrivilegeRequest request) {
        return request.getBaseAnnualFee() == null && request.getBaseCashBackRate() == null;
    }

    private void setNonNullFieldsToUpdateCardPrivilege(UpdateCardPrivilegeRequest request, CardPrivilege existingCardPrivilege) {
        if (request.getBaseAnnualFee() != null) {
            existingCardPrivilege.setBaseAnnualFee(request.getBaseAnnualFee());
        }

        if (request.getBaseCashBackRate() != null) {
            existingCardPrivilege.setBaseCashbackRate(request.getBaseCashBackRate());
        }
    }
}
