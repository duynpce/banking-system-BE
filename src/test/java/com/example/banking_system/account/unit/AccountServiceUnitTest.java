package com.example.banking_system.account.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.dto.EditPasswordRequest;
import com.example.banking_system.domain.account.dto.GetAccountResponse;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.domain.account.service.domain.AccountService;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;


import static org.mockito.Mockito.*;

public class AccountServiceUnitTest extends UnitTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    JwtUtil jwtUtil;

    @Mock
    AccountMapper accountMapper;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void getAccountSuccess() {
        final String username = "username";
        when(jwtUtil.getUsername()).thenReturn(username);
        GetAccountResponse response = new GetAccountResponse();
        Account returnAccount = accountTestCases.getBusinessAccountTestCase().getAccount();
        response.setEmail(returnAccount.getEmail());

        when(accountQueryService.findByUsername(username)).thenReturn(returnAccount);
        when(accountMapper.toDto(returnAccount)).thenReturn(response);

        GetAccountResponse result = accountService.get();

        Assertions.assertEquals(returnAccount.getEmail(), result.getEmail());
        verify(accountQueryService).findByUsername(username);
    }

    @Test
    public void getAccountFailure_UserNotFound() {
        final String username = "nonexistent";

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenThrow(new NotFoundException("User not found with username: " + username));

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> accountService.get());

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountQueryService).findByUsername(username);
    }

    @Test
    public void deleteSuccess() {
        final String username = "username";
        when(jwtUtil.getUsername()).thenReturn(username);
        BusinessAccount account = accountTestCases.getBusinessAccountTestCase();

        when(accountQueryService.findByUsername(username)).thenReturn(account.getAccount());
        ReflectionTestUtils.setField(accountService,"ENVIRONMENT", "test");
        doNothing().when(accountQueryService).delete(account.getAccount());

        accountService.delete();

        verify(accountQueryService).findByUsername(username);
        verify(accountQueryService).delete(account.getAccount());
    }

    @Test
    public void deleteFailure_UserNotFound() {
        final String username = "nonexistent";

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenThrow(new NotFoundException("User not found with username: " + username));
        ReflectionTestUtils.setField(accountService,"ENVIRONMENT", "test");


        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> accountService.delete());

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountQueryService).findByUsername(username);
        verify(accountQueryService, never()).delete(any());
    }

    @Test
    public void editPasswordSuccess() {
        final String username = "username";
        final String oldPassword = "OldPassword123@";
        final String newPassword = "NewPassword123@";
        final String encodedPassword = "encodedPassword";

        EditPasswordRequest request = new EditPasswordRequest();
        request.setCurrentPassword(oldPassword);
        request.setNewPassword(newPassword);

        Account account = accountTestCases.getBusinessAccountTestCase().getAccount();
        account.setPassword("existingHashedPassword");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(passwordEncoder.matches(oldPassword, account.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        accountService.editPassword(request);

        Assertions.assertEquals(encodedPassword, account.getPassword());
        verify(accountQueryService).save(account);
    }

    @Test
    public void editPasswordFailure_currentPasswordIncorrect() {
        final String username = "username";

        EditPasswordRequest request = new EditPasswordRequest();
        request.setCurrentPassword("WrongOldPassword123@");
        request.setNewPassword("NewPassword123@");

        Account account = accountTestCases.getBusinessAccountTestCase().getAccount();
        account.setPassword("existingHashedPassword");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())).thenReturn(false);

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> accountService.editPassword(request)
        );

        Assertions.assertEquals("password is incorrect", exception.getMessage());
        verify(accountQueryService, never()).save(any());
    }

    @Test
    public void editPasswordFailure_UpdatedWithin48Hours() {
        final String username = "username";

        EditPasswordRequest request = new EditPasswordRequest();
        request.setCurrentPassword("OldPassword123@");
        request.setNewPassword("NewPassword123@");

        Account account = accountTestCases.getBusinessAccountTestCase().getAccount();
        account.setUpdatedPasswordAt(Instant.now());

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);

        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> accountService.editPassword(request)
        );

        Assertions.assertEquals("password can only be changed once every 48 hours", exception.getMessage());
        verify(accountQueryService, never()).save(any());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
