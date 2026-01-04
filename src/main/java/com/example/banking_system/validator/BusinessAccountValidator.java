package com.example.banking_system.validator;

import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.repository.account.BusinessAccountRepository;
import com.example.banking_system.service.account.BusinessAccountService;
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
        accountValidator.validateCreate(businessAccount);
        validateUniqueAccountBusinessDetails(businessAccount);

    }

    public void validateUniqueAccountBusinessDetails(BusinessAccount businessAccount) {
       util.assertNotConflictData(businessAccountRepository.existsByOrganizationName(businessAccount.getOrganizationName()),"Organization name already exists");
       util.assertNotConflictData(businessAccountRepository.existsByTaxIdNumber(businessAccount.getTaxIdNumber()),"Tax id number already exists");
    }
}
