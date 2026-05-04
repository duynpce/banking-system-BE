package com.example.banking_system.domain.account.service.query;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.repository.AccountRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService {
    private final AccountRepository accountRepository;

    @Getter
    @Value("${value.transaction.deposit-account-number}")
    private String INTERNAL_DEPOSIT_ACCOUNT_NUMBER;

    @Getter
    @Value("${value.transaction.withdrawal-account-number}")
    private String INTERNAL_WITHDRAWAL_ACCOUNT_NUMBER;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(long id ){
        return accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found with id: " + id)
        );
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber).orElseThrow(
                () -> new NotFoundException("Account not found with account number: " + accountNumber)
        );
    }

    public Account getInternalDePositAccount() {
        return findByAccountNumber(INTERNAL_DEPOSIT_ACCOUNT_NUMBER);
    }

    public Account getInternalWithdrawalAccount() {
        return findByAccountNumber(INTERNAL_WITHDRAWAL_ACCOUNT_NUMBER);
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

    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByNumber(accountNumber);
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }
}
