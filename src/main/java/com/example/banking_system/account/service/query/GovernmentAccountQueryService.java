package com.example.banking_system.account.service.query;

import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.repository.GovernmentAccountRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GovernmentAccountQueryService {
    private final GovernmentAccountRepository governmentAccountRepository;


    public GovernmentAccount save(GovernmentAccount governmentAccount) {
        return governmentAccountRepository.save(governmentAccount);
    }

    public GovernmentAccount findByUsername(String username) {
        return governmentAccountRepository.findByAccount_Username(username)
                .orElseThrow(() -> new NotFoundException("Government account not found with username: " + username));
    }

}
