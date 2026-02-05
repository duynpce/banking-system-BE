package com.example.banking_system.account.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.account.dto.GetAccountResponse;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.service.domain.AccountService;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.mockito.Mockito.*;

public class AccountServiceUnitTest extends UnitTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    JwtUtil jwtUtil;

    @Mock
    AccountMapper accountMapper;

    @Mock
    private AccountQueryService accountQueryService;

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


        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> accountService.delete());

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountQueryService).findByUsername(username);
        verify(accountQueryService, never()).delete(any());
    }
}
