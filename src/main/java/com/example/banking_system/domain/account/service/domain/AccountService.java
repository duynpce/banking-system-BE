package com.example.banking_system.domain.account.service.domain;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.dto.EditPasswordRequest;
import com.example.banking_system.domain.account.dto.GetAccountResponse;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.domain.account.entity.PersonalAccount;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountQueryService accountQueryService;
    private final AccountMapper accountMapper;
    private final SecureRandom secureRandom;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${value.environment}")
    private String ENVIRONMENT;

    @Transactional(readOnly = true)
    public GetAccountResponse get() {
        String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        return accountMapper.toDto(account);
    }

    @Transactional
    public String getNameByAccountNumber(String accountNumber) {
        Account account = accountQueryService.findByAccountNumber(accountNumber);

        switch (account.getType()) {
            case PERSONAL -> {
                return ((PersonalAccount) account.getAccountDetails()).getFullName();
            }
            case BUSINESS -> {
                return ((BusinessAccount) account.getAccountDetails()).getOrganizationName();
            }
            case GOVERNMENT -> {
                return ((GovernmentAccount) account.getAccountDetails()).getGovernmentDepartment();
            }
            default -> throw new ValidationException("Invalid account type");
        }
    }

    @Transactional
    public void delete() {
        if(ENVIRONMENT.equals("prod")) {
            throw new ValidationException("Account deletion is not allowed in production environment");
        }

        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        accountQueryService.delete(account);
    }

    @Transactional
    public void editPassword(EditPasswordRequest request) {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        boolean changedPasswordIn48Hours = account.getUpdatedPasswordAt() != null
                && account.getUpdatedPasswordAt().plus(48, ChronoUnit.HOURS).isAfter(Instant.now());

        if (changedPasswordIn48Hours) {
            throw new ValidationException("password can only be changed once every 48 hours");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new ValidationException("password is incorrect");
        }


        if(passwordEncoder.matches(request.getNewPassword(), account.getPassword())) {
            throw new ValidationException("new password must be different from the current password");
        }

        Instant now = Instant.now();
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        account.setUpdatedPasswordAt(now);
        accountQueryService.save(account);
    }

    public String generateAccountNumber() {
        String accountNumber;

        do {
            long randomNumber = secureRandom.nextLong(secureRandom.nextLong(1000000000000L));
            accountNumber = "%012d".formatted(randomNumber);
        } while (accountQueryService.existsByAccountNumber(accountNumber)); // Ensure uniqueness

        return accountNumber;
    }

}
