package com.example.banking_system.account.service.domain;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.service.query.BusinessAccountQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.BusinessAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessAccountService {
    private final BusinessAccountQueryService businessAccountQueryService;
    private final AccountMapper accountMapper;
    private final BusinessAccountValidator businessAccountValidator;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @Transactional
    public BusinessAccount create(CreateBusinessAccountRequest createBusinessAccountRequest) {
        BusinessAccount businessAccount = accountMapper.toEntity(createBusinessAccountRequest);
        businessAccountValidator.validateCreate(businessAccount);

        final String hashedPassword = passwordEncoder.encode(createBusinessAccountRequest.getPassword());
        businessAccount.getAccount().setPassword(hashedPassword);

        String accountNumber = accountService.generateAccountNumber();
        businessAccount.getAccount().setAccountNumber(accountNumber);

        return businessAccountQueryService.save(businessAccount);

    }

    @Transactional
    public BusinessAccount update(UpdateBusinessAccountRequest request) {
        String username = jwtUtil.getUsername();
        BusinessAccount existingAccount = businessAccountQueryService.findByUsername(username);
        businessAccountValidator.validateUpdate(request, existingAccount);

        return businessAccountQueryService.save(existingAccount);
    }

}
