package com.example.banking_system.account.service;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.GovernmentAccountRepository;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.GovernmentAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GovernmentAccountService {
    private final GovernmentAccountRepository governmentAccountRepository;
    private final AccountMapper accountMapper;
    private final GovernmentAccountValidator governmentAccountValidator;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public GovernmentAccount create(CreateGovernmentAccountRequest createGovernmentAccountRequest) {
        GovernmentAccount governmentAccount = accountMapper.toEntity(createGovernmentAccountRequest);
        governmentAccountValidator.validateCreate(governmentAccount);

        final String hashedPassword = passwordEncoder.encode(createGovernmentAccountRequest.getPassword());
        governmentAccount.setPassword(hashedPassword);

        return governmentAccountRepository.save(governmentAccount);
    }

    public GovernmentAccount update(UpdateGovernmentAccountRequest request) {
        String username = jwtUtil.getUsername();
        GovernmentAccount existingAccount = (GovernmentAccount) accountService.findByUsernameAndType(username, AccountType.GOVERNMENT);
        governmentAccountValidator.validateUpdate(request, existingAccount);

        return governmentAccountRepository.save(existingAccount);
    }
}

