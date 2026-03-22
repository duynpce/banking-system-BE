package com.example.banking_system.domain.account.service.domain;

import com.example.banking_system.domain.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.domain.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.domain.account.service.query.GovernmentAccountQueryService;
import com.example.banking_system.domain.account.validator.GovernmentAccountValidator;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GovernmentAccountService {
    private final GovernmentAccountQueryService governmentAccountQueryService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final GovernmentAccountValidator governmentAccountValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public GovernmentAccount create(CreateGovernmentAccountRequest createGovernmentAccountRequest) {
        GovernmentAccount governmentAccount = accountMapper.toEntity(createGovernmentAccountRequest);
        governmentAccountValidator.validateCreate(governmentAccount);

        final String hashedPassword = passwordEncoder.encode(createGovernmentAccountRequest.getPassword());
        governmentAccount.getAccount().setPassword(hashedPassword);

        String accountNumber = accountService.generateAccountNumber();
        governmentAccount.getAccount().setAccountNumber(accountNumber);

        return governmentAccountQueryService.save(governmentAccount);
    }

    @Transactional
    public GovernmentAccount update(UpdateGovernmentAccountRequest request) {
        String username = jwtUtil.getUsername();
        GovernmentAccount existingAccount = governmentAccountQueryService.findByUsername(username);
        governmentAccountValidator.validateUpdate(request, existingAccount);

        return governmentAccountQueryService.save(existingAccount);
    }

}
