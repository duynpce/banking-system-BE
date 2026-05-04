package com.example.banking_system.domain.account.validator;

import com.example.banking_system.domain.account.dto.UpdatePersonalAccountRequest;
import com.example.banking_system.domain.account.entity.PersonalAccount;
import com.example.banking_system.domain.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class PersonalAccountValidator {
    private final AccountValidator accountValidator;
    private final PersonalAccountQueryService personalAccountQueryService;
    private final Util util;

    public void validateCreate(PersonalAccount personalAccount) {
        accountValidator.validateCreate(personalAccount.getAccount());
        validateUniqueAccountPersonalDetails(personalAccount);
        validateAdult(personalAccount.getDateOfBirth());
    }

    public void validateUniqueAccountPersonalDetails(PersonalAccount personalAccount) {
        util.assertUnique(personalAccountQueryService.existsByIdCardNumber(personalAccount.getIdCardNumber()), "ID card number already exists");
    }

    public void validateUpdate(UpdatePersonalAccountRequest request, PersonalAccount existingAccount) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }
        // check if the fields to be updated are unique, if they are unique, set them to existingAccount
        accountValidator.validateUpdate(request, existingAccount.getAccount());
        accountValidator.setNonNullFieldsToUpdateAccount(request, existingAccount.getAccount());
        setNonNullFieldsToUpdatePersonalAccount(request, existingAccount);
    }

    private boolean isAllFieldsNull(UpdatePersonalAccountRequest request) {
        return request.getEmail() == null && request.getPhoneNumber() == null && request.getAddress() == null && request.getGender() == null
               && request.getFullName() == null && request.getDateOfBirth() == null && request.getIdCardNumber() == null;
    }

    private void setNonNullFieldsToUpdatePersonalAccount(UpdatePersonalAccountRequest request, PersonalAccount existingAccount) {
        if (request.getFullName() != null) {
            existingAccount.setFullName(request.getFullName());
        }

        if (request.getDateOfBirth() != null) {
            validateAdult(request.getDateOfBirth());
            existingAccount.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getIdCardNumber() != null && !request.getPhoneNumber().equals(existingAccount.getIdCardNumber()) &&
        !request.getIdCardNumber().equals(existingAccount.getIdCardNumber())) {
            util.assertUnique(personalAccountQueryService.existsByIdCardNumber(request.getIdCardNumber()), "ID card number already exists");
            existingAccount.setIdCardNumber(request.getIdCardNumber());
        }

        if (request.getGender() != null &&  request.getGender().equals(existingAccount.getGender())) {
            existingAccount.setGender(request.getGender());
        }
    }

    private void validateAdult(LocalDate dateOfBirth) {
        LocalDate todayUtc = LocalDate.now(ZoneOffset.UTC);
        if (dateOfBirth.isAfter(todayUtc.minusYears(18))) {
            throw new ValidationException("date of birth must be at least 18 years old");
        }
    }
}
