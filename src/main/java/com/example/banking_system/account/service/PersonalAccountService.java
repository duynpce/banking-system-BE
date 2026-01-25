package com.example.banking_system.account.service;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.account.dto.UpdatePersonalAccountRequest;
import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.PersonalAccountRepository;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.PersonalAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalAccountService {
    private final PersonalAccountRepository personalAccountRepository;
    private final AccountMapper accountMapper;
    private final PersonalAccountValidator personalAccountValidator;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public PersonalAccount create(CreatePersonalAccountRequest createPersonalAccountRequest) {
        PersonalAccount personalAccount = accountMapper.toEntity(createPersonalAccountRequest);
        personalAccountValidator.validateCreate(personalAccount);

        final String hashedPassword = passwordEncoder.encode(createPersonalAccountRequest.getPassword());
        personalAccount.setPassword(hashedPassword);

        return personalAccountRepository.save(personalAccount);
    }

    public PersonalAccount update(UpdatePersonalAccountRequest request) {
        String username = jwtUtil.getUsername();
        PersonalAccount existingAccount = (PersonalAccount) accountService.findByUsernameAndType(username, AccountType.PERSONAL);
        personalAccountValidator.validateUpdate(request, existingAccount);

        return personalAccountRepository.save(existingAccount);
    }
}
