package com.example.banking_system.domain.account.validator;

import com.example.banking_system.domain.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GovernmentAccountValidator {
    private final AccountValidator accountValidator;

    public void validateCreate(GovernmentAccount governmentAccount) {
        accountValidator.validateCreate(governmentAccount.getAccount());
    }

    public void validateUpdate(UpdateGovernmentAccountRequest request, GovernmentAccount existingAccount) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // check if the fields to be updated are unique, if they are unique, set them to existingAccount
        accountValidator.validateUpdate(request, existingAccount.getAccount());
        accountValidator.setNonNullFieldsToUpdateAccount(request, existingAccount.getAccount());
        setNonNullFieldsToUpdateGovernmentAccount(request, existingAccount);
    }

    private boolean isAllFieldsNull(UpdateGovernmentAccountRequest request) {
        return request.getEmail() == null && request.getPhoneNumber() == null && request.getAddress() == null &&
               request.getGovernmentDepartment() == null;
    }

    private void setNonNullFieldsToUpdateGovernmentAccount(UpdateGovernmentAccountRequest request, GovernmentAccount existingAccount) {
        if (request.getGovernmentDepartment() != null) {
            existingAccount.setGovernmentDepartment(request.getGovernmentDepartment());
        }
    }
}
