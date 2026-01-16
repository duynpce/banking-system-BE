package com.example.banking_system.service.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.dto.account.UpdateBusinessAccountRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.exception.ForbiddenException;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.BusinessAccountRepository;
import com.example.banking_system.utility.JwtUtil;
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
