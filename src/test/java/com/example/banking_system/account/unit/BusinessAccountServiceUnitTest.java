package com.example.banking_system.account.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.service.domain.AccountService;
import com.example.banking_system.domain.account.service.domain.BusinessAccountService;
import com.example.banking_system.domain.account.service.query.BusinessAccountQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.domain.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.domain.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.validator.BusinessAccountValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusinessAccountServiceUnitTest extends UnitTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    BusinessAccountQueryService businessAccountQueryService;

    @Mock
    BusinessAccountValidator businessAccountValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccountService accountService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    BusinessAccountService businessAccountService;

    @Test
    public void createAccountSuccess() {
        BusinessAccount businessAccount = accountTestCases.getBusinessAccountTestCase();
        final String hashedPassword = "hashedPassword";
        final String mockAccountNumber = "mockAccountNumber";

        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(businessAccount);
        doNothing().when(businessAccountValidator).validateCreate(businessAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(businessAccountQueryService.save(businessAccount)).thenReturn(businessAccount);
        when(accountService.generateAccountNumber()).thenReturn(mockAccountNumber);

        BusinessAccount createdAccount = businessAccountService.create(request);

        assertEquals(businessAccount, createdAccount);
        assertEquals(mockAccountNumber, businessAccount.getAccount().getNumber());

        verify(businessAccountQueryService, times(1)).save(businessAccount);
    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        BusinessAccount invalidAccount = new BusinessAccount();

        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(businessAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> businessAccountService.create(request));

        assertEquals("invalid account", exception.getMessage());
        verify(businessAccountQueryService, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        BusinessAccount existingAccount = accountTestCases.getBusinessAccountTestCase();
        String username = existingAccount.getAccount().getUsername();

        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        request.setOrganizationName("NewOrganizationName");
        request.setEmail("newemail@example.com");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(businessAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doNothing().when(businessAccountValidator).validateUpdate(request, existingAccount);
        when(businessAccountQueryService.save(existingAccount)).thenReturn(existingAccount);

        BusinessAccount updatedAccount = businessAccountService.update(request);

        assertEquals(existingAccount, updatedAccount);
        verify(businessAccountValidator).validateUpdate(request, existingAccount);
        verify(businessAccountQueryService).save(existingAccount);
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        BusinessAccount existingAccount = accountTestCases.getBusinessAccountTestCase();
        String username = existingAccount.getAccount().getUsername();

        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(businessAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(businessAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> businessAccountService.update(request));

        assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(businessAccountQueryService, never()).save(any());
    }

    @Test
    public void updateAccountFailure_UpdatedWithin48Hours() {
        BusinessAccount existingAccount = accountTestCases.getBusinessAccountTestCase();
        existingAccount.getAccount().setUpdatedAt(Instant.now());
        String username = existingAccount.getAccount().getUsername();

        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        request.setOrganizationName("NewOrganizationName");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(businessAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doThrow(new ValidationException("account can only be updated once every 48 hours"))
                .when(businessAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> businessAccountService.update(request));

        assertEquals("account can only be updated once every 48 hours", exception.getMessage());
        verify(businessAccountQueryService, never()).save(any());
    }
}
