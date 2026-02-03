package com.example.banking_system.account.service.query;

import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.account.repository.PersonalAccountRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalAccountQueryService {
    private final PersonalAccountRepository personalAccountRepository;

    public PersonalAccount save(PersonalAccount personalAccount) {
        return personalAccountRepository.save(personalAccount);
    }

    public PersonalAccount findByUsername(String username) {
        return personalAccountRepository.findByAccount_Username(username)
                .orElseThrow(() -> new NotFoundException("Personal account not found with username: " + username));
    }

    public boolean existsByIdCardNumber(String idCardNumber) {
        return personalAccountRepository.existsByIdCardNumber(idCardNumber);
    }
}
