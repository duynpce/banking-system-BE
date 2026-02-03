package com.example.banking_system.account.service.domain;

import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.account.dto.UpdatePersonalAccountRequest;
import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.PersonalAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalAccountService {
    private final PersonalAccountQueryService personalAccountQueryService;
    private final AccountMapper accountMapper;
    private final PersonalAccountValidator personalAccountValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public PersonalAccount create(CreatePersonalAccountRequest createPersonalAccountRequest) {
        PersonalAccount personalAccount = accountMapper.toEntity(createPersonalAccountRequest);
        personalAccountValidator.validateCreate(personalAccount);

        final String hashedPassword = passwordEncoder.encode(createPersonalAccountRequest.getPassword());
        personalAccount.getAccount().setPassword(hashedPassword);

        return personalAccountQueryService.save(personalAccount);
    }

    @Transactional
    public PersonalAccount update(UpdatePersonalAccountRequest request) {
        String username = jwtUtil.getUsername();
        PersonalAccount existingAccount = personalAccountQueryService.findByUsername(username);
        personalAccountValidator.validateUpdate(request, existingAccount);

        return personalAccountQueryService.save(existingAccount);
    }
}
