package com.example.banking_system.service.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.GetAccountRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
import com.example.banking_system.exception.NotFoundException;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    // get --> used for GET method , find --> used for internal finding without mapping
    public GetAccountRequest getByUsername(String username) {
        Account account = findByUsername(username);
        return mapToGetDto(account);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
    }

    public GetAccountRequest mapToGetDto(Account account) {

        if(account.getType() == AccountType.PERSONAL) {
            return accountMapper.toDto((PersonalAccount) account);
        } else if(account.getType() == AccountType.BUSINESS) {
            return accountMapper.toDto((BusinessAccount) account);
        } else if(account.getType() == AccountType.GOVERNMENT) {
            return accountMapper.toDto((GovernmentAccount) account);
        }

            throw new NotFoundException("Unknown account type");
    }

    public Account findByUsernameAndType(String username, AccountType type) {
        Account account = findByUsername(username);

        if(account.getType() != type) {
            throw new NotFoundException("account type mismatch");
        }

        return account;
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return accountRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public void delete(String username) {
        Account account = findByUsername(username);
        accountRepository.delete(account);
    }
}
