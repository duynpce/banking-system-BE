package com.example.banking_system.account.service;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.BusinessAccountRepository;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.BusinessAccountValidator;
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
    private final AccountService accountService;
    private final JwtUtil jwtUtil;


    public BusinessAccount create(CreateBusinessAccountRequest createBusinessAccountRequest) {
        BusinessAccount businessAccount = accountMapper.toEntity(createBusinessAccountRequest);
        businessAccountValidator.validateCreate(businessAccount);

        final String hashedPassword = passwordEncoder.encode(createBusinessAccountRequest.getPassword());
        businessAccount.setPassword(hashedPassword);

        return businessAccountRepository.save(businessAccount);
    }

    public BusinessAccount update(UpdateBusinessAccountRequest request) {
        String username = jwtUtil.getUsername();
        BusinessAccount existingAccount = (BusinessAccount) accountService.findByUsernameAndType(username, AccountType.BUSINESS);
        businessAccountValidator.validateUpdate(request, existingAccount);

        return businessAccountRepository.save(existingAccount);
    }

}
