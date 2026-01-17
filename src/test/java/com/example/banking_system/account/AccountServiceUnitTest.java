package com.example.banking_system.account;

import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.GetAccountRequest;
import com.example.banking_system.dto.account.GetBusinessAccountRequest;
import com.example.banking_system.dto.account.GetGovernmentAccountRequest;
import com.example.banking_system.dto.account.GetPersonalAccountRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
import com.example.banking_system.exception.ForbiddenException;
import com.example.banking_system.exception.NotFoundException;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.AccountRepository;
import com.example.banking_system.service.account.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    private final BusinessAccount businessTestCase = new BusinessAccount("username", "password", "email", "phoneNumber", "address", "OrganizationName", "TaxIdNumber");
    private final PersonalAccount personalTestCase = new PersonalAccount("username", "password", "email", "phoneNumber", "address", "fullName", LocalDate.now(), "idCardNumber");
    private final GovernmentAccount governmentTestCase = new GovernmentAccount("username", "password", "email", "phoneNumber", "address", "governmentDepartment");

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void getByUsernameSuccess() {
        final String username = "username";
        BusinessAccount account = businessTestCase;
        GetBusinessAccountRequest dto = new GetBusinessAccountRequest();

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        when(accountMapper.toDto((BusinessAccount) account)).thenReturn(dto);

        GetAccountRequest result = accountService.getByUsername(username);

        Assertions.assertEquals(dto, result);
        verify(accountRepository).findByUsername(username);
        verify(accountMapper).toDto((BusinessAccount) account);
    }

    @Test
    public void getByUsernameFailure_UserNotFound() {
        final String username = "nonexistent";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            accountService.getByUsername(username);
        });

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountRepository).findByUsername(username);
        verify(accountMapper, never()).toDto(any(BusinessAccount.class));
    }

    @Test
    public void mapToGetDtoSuccess_BusinessAccount() {
        BusinessAccount account = businessTestCase;
        GetBusinessAccountRequest dto = new GetBusinessAccountRequest();

        when(accountMapper.toDto((BusinessAccount) account)).thenReturn(dto);

        GetAccountRequest result = accountService.mapToGetDto(account);

        Assertions.assertEquals(dto, result);
        verify(accountMapper).toDto((BusinessAccount) account);
    }

    @Test
    public void mapToGetDtoSuccess_PersonalAccount() {
        PersonalAccount account = personalTestCase;
        GetPersonalAccountRequest dto = new GetPersonalAccountRequest();

        when(accountMapper.toDto((PersonalAccount) account)).thenReturn(dto);

        GetAccountRequest result = accountService.mapToGetDto(account);

        Assertions.assertEquals(dto, result);
        verify(accountMapper).toDto((PersonalAccount) account);
    }

    @Test
    public void mapToGetDtoSuccess_GovernmentAccount() {
        GovernmentAccount account = governmentTestCase;
        GetGovernmentAccountRequest dto = new GetGovernmentAccountRequest();

        when(accountMapper.toDto((GovernmentAccount) account)).thenReturn(dto);

        GetAccountRequest result = accountService.mapToGetDto(account);

        Assertions.assertEquals(dto, result);
        verify(accountMapper).toDto((GovernmentAccount) account);
    }

    @Test
    public void mapToGetDtoFailure_UnknownAccountType() {
        Account account = mock(Account.class);
        when(account.getType()).thenReturn(null);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            accountService.mapToGetDto(account);
        });

        Assertions.assertEquals("Unknown account type", exception.getMessage());
    }

    @Test
    public void findByUsernameAndTypeSuccess() {
        final String username = "username";
        BusinessAccount account = businessTestCase;

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        Account result = accountService.findByUsernameAndType(username, AccountType.BUSINESS);

        Assertions.assertEquals(account, result);
        verify(accountRepository).findByUsername(username);
    }

    @Test
    public void findByUsernameAndTypeFailure_TypeMismatch() {
        final String username = "username";
        BusinessAccount account = businessTestCase;

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            accountService.findByUsernameAndType(username, AccountType.PERSONAL);
        });

        Assertions.assertEquals("account type mismatch", exception.getMessage());
        verify(accountRepository).findByUsername(username);
    }

    @Test
    public void deleteSuccess() {
        final String username = "username";
        BusinessAccount account = businessTestCase;

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        accountService.delete(username);

        verify(accountRepository).findByUsername(username);
        verify(accountRepository).delete(account);
    }

    @Test
    public void deleteFailure_UserNotFound() {
        final String username = "nonexistent";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            accountService.delete(username);
        });

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountRepository).findByUsername(username);
        verify(accountRepository, never()).delete(any());
    }
}

