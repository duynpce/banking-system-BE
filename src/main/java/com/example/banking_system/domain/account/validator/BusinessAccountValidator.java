package com.example.banking_system.domain.account.validator;

import com.example.banking_system.domain.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.domain.account.service.query.BusinessAccountQueryService;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessAccountValidator {
    private final AccountValidator accountValidator;
    private final BusinessAccountQueryService businessAccountQueryService;
    private final Util util;

    public void validateCreate(BusinessAccount businessAccount) {
        accountValidator.validateUniqueAccountDetails(businessAccount.getAccount());
        validateUniqueAccountBusinessDetails(businessAccount);
    }

    public void validateUniqueAccountBusinessDetails(BusinessAccount businessAccount) {
       util.assertUnique(businessAccountQueryService.existsByTaxIdNumber(businessAccount.getTaxIdNumber()),"Tax id number already exists");
    }

    public void validateUpdate(UpdateBusinessAccountRequest request, BusinessAccount existingAccount) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // check if the fields to be updated are unique, if they are unique, set them to existingAccount
        accountValidator.setNonNullFieldsToUpdateAccount(request, existingAccount.getAccount());
        setNonNullFieldsToUpdateBusinessAccount(request, existingAccount);
    }

    private boolean isAllFieldsNull(UpdateBusinessAccountRequest request) {
        return request.getEmail() == null && request.getPhoneNumber() == null && request.getAddress() == null &&
               request.getOrganizationName() == null && request.getTaxIdNumber() == null;
    }

    private void setNonNullFieldsToUpdateBusinessAccount(UpdateBusinessAccountRequest request, BusinessAccount existingAccount) {
        if (request.getOrganizationName() != null) {
            existingAccount.setOrganizationName(request.getOrganizationName());
        }

        if (request.getTaxIdNumber() != null) {
            util.assertUnique(businessAccountQueryService.existsByTaxIdNumber(request.getTaxIdNumber()), "Tax id number already exists");
            existingAccount.setTaxIdNumber(request.getTaxIdNumber());
        }
    }
}
