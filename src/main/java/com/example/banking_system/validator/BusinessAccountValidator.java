package com.example.banking_system.validator;

import com.example.banking_system.dto.account.UpdateBusinessAccountRequest;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.exception.ValidationException;
import com.example.banking_system.repository.account.BusinessAccountRepository;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessAccountValidator {
    private final AccountValidator accountValidator;
    private final BusinessAccountRepository businessAccountRepository;
    private final Util util;

    public void validateCreate(BusinessAccount businessAccount) {
        accountValidator.validateUniqueAccountDetails(businessAccount);
        validateUniqueAccountBusinessDetails(businessAccount);

    }

    public void validateUniqueAccountBusinessDetails(BusinessAccount businessAccount) {
       util.assertUnique(businessAccountRepository.existsByTaxIdNumber(businessAccount.getTaxIdNumber()),"Tax id number already exists");
    }

    public void validateUpdate(UpdateBusinessAccountRequest request, BusinessAccount existingAccount) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // check if the fields to be updated are unique, if they are unique, set them to existingAccount
        accountValidator.setNonNullFieldsToUpdateAccount(request, existingAccount);
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
            util.assertUnique(businessAccountRepository.existsByTaxIdNumber(request.getTaxIdNumber()), "Tax id number already exists");
            existingAccount.setTaxIdNumber(request.getTaxIdNumber());
        }
    }


}
