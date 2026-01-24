package com.example.banking_system.service.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.CreatePersonalAccountRequest;
import com.example.banking_system.dto.account.UpdatePersonalAccountRequest;
import com.example.banking_system.entity.account.PersonalAccount;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.PersonalAccountRepository;
import com.example.banking_system.utility.JwtUtil;
import com.example.banking_system.validator.account.PersonalAccountValidator;
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
