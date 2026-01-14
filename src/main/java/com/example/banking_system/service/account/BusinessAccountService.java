package com.example.banking_system.service.account;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.BusinessAccountRepository;
import com.example.banking_system.validator.BusinessAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessAccountService {
    private final BusinessAccountRepository businessAccountRepository;
    private final AccountMapper accountMapper;
    private final BusinessAccountValidator businessAccountValidator;
    private final PasswordEncoder passwordEncoder;


    public BusinessAccount create(CreateBusinessAccountRequest createBusinessAccountRequest) {
        BusinessAccount businessAccount = accountMapper.toBusinessAccount(createBusinessAccountRequest);
        businessAccountValidator.validateCreate(businessAccount);

        final String hashedPassword = passwordEncoder.encode(createBusinessAccountRequest.getPassword());
        businessAccount.setPassword(hashedPassword);

        return businessAccountRepository.save(businessAccount);
    }
}
