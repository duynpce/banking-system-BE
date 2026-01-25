package com.example.banking_system.account.service;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.GetAccountResponse;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    /*
        2 steps:
            1 find account by username
            2 map to corresponding GetAccountResponse dto
     */
    public GetAccountResponse getByUsername(String username) {
        Account account = findByUsername(username);
        return mapToGetDto(account);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
    }

    public GetAccountResponse mapToGetDto(Account account) {

        if(account.getType() == AccountType.PERSONAL) {
            return accountMapper.toDto((PersonalAccount) account);
        } else if(account.getType() == AccountType.BUSINESS) {
            return accountMapper.toDto((BusinessAccount) account);
        } else if(account.getType() == AccountType.GOVERNMENT) {
            return accountMapper.toDto((GovernmentAccount) account);
        }

            throw new NotFoundException("Unknown account type");
    }

    /*
        2 steps:
            1 find account by username
            2 check if account type matches the provided type
     */
    public Account findByUsernameAndType(String username, AccountType type) {
        Account account = findByUsername(username);

        if(account.getType() != type) {
            throw new ForbiddenException("account type mismatch");
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

    public void deleteByUsername(String username) {
        Account account = findByUsername(username);
        accountRepository.delete(account);
    }
}
