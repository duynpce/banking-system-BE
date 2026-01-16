package com.example.banking_system.validator;

import com.example.banking_system.dto.account.UpdatePersonalAccountRequest;
import com.example.banking_system.entity.account.PersonalAccount;
import com.example.banking_system.exception.ValidationException;
import com.example.banking_system.repository.account.PersonalAccountRepository;
import com.example.banking_system.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalAccountValidator {
    private final AccountValidator accountValidator;
    private final PersonalAccountRepository personalAccountRepository;
    private final Util util;

    public void validateCreate(PersonalAccount personalAccount) {
        accountValidator.validateUniqueAccountDetails(personalAccount);
        validateUniqueAccountPersonalDetails(personalAccount);
    }

    public void validateUniqueAccountPersonalDetails(PersonalAccount personalAccount) {
        util.assertUnique(personalAccountRepository.existsByIdCardNumber(personalAccount.getIdCardNumber()), "ID card number already exists");
    }

    public void validateUpdate(UpdatePersonalAccountRequest request, PersonalAccount existingAccount) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        // check if the fields to be updated are unique, if they are unique, set them to existingAccount
        accountValidator.setNonNullFieldsToUpdateAccount(request, existingAccount);
        setNonNullFieldsToUpdatePersonalAccount(request, existingAccount);
    }

    private boolean isAllFieldsNull(UpdatePersonalAccountRequest request) {
        return request.getEmail() == null && request.getPhoneNumber() == null && request.getAddress() == null &&
               request.getFullName() == null && request.getDateOfBirth() == null && request.getIdCardNumber() == null;
    }

    private void setNonNullFieldsToUpdatePersonalAccount(UpdatePersonalAccountRequest request, PersonalAccount existingAccount) {
        if (request.getFullName() != null) {
            existingAccount.setFullName(request.getFullName());
        }

        if (request.getDateOfBirth() != null) {
            existingAccount.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getIdCardNumber() != null) {
            util.assertUnique(personalAccountRepository.existsByIdCardNumber(request.getIdCardNumber()), "ID card number already exists");
            existingAccount.setIdCardNumber(request.getIdCardNumber());
        }
    }
}
