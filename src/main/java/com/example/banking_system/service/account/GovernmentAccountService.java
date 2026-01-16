package com.example.banking_system.service.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.CreateGovernmentAccountRequest;
import com.example.banking_system.dto.account.UpdateGovernmentAccountRequest;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.GovernmentAccountRepository;
import com.example.banking_system.utility.JwtUtil;
import com.example.banking_system.validator.GovernmentAccountValidator;
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
        GovernmentAccount governmentAccount = accountMapper.toGovernmentAccount(createGovernmentAccountRequest);
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

