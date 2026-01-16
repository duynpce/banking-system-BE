package com.example.banking_system.service.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.exception.NotFoundException;
import com.example.banking_system.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
    }

    public Account findByUsernameAndType(String username, AccountType type) {
      Account account = findByUsername(username);

      if(account.getType() != type) {
          throw new NotFoundException("User not found with username: " + username + " and type: " + type);
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
