package com.example.banking_system.account.service.query;

import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.repository.BusinessAccountRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessAccountQueryService {
    private final BusinessAccountRepository businessAccountRepository;

    public BusinessAccount save(BusinessAccount businessAccount) {
        return businessAccountRepository.save(businessAccount);
    }

    public BusinessAccount findByUsername(String username) {
        return businessAccountRepository.findByAccount_Username(username)
                .orElseThrow(() -> new NotFoundException("Business account not found with username: " + username));
    }

    public boolean existsByTaxIdNumber(String taxIdNumber) {
        return businessAccountRepository.existsByTaxIdNumber(taxIdNumber);
    }
}
