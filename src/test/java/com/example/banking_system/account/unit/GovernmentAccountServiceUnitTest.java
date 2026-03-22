package com.example.banking_system.account.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.service.domain.AccountService;
import com.example.banking_system.domain.account.service.domain.GovernmentAccountService;
import com.example.banking_system.domain.account.service.query.GovernmentAccountQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.domain.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.domain.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.mapper.AccountMapper;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.validator.GovernmentAccountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GovernmentAccountServiceUnitTest extends UnitTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    GovernmentAccountQueryService governmentAccountQueryService;

    @Mock
    GovernmentAccountValidator governmentAccountValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccountService accountService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    GovernmentAccountService governmentAccountService;

    @Test
    public void createAccountSuccess() {
        GovernmentAccount governmentAccount = accountTestCases.getGovernmentAccountTestCase();
        final String hashedPassword = "hashedPassword";
        final String mockAccountNumber = "mockAccountNumber";

        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(governmentAccount);
        doNothing().when(governmentAccountValidator).validateCreate(governmentAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(governmentAccountQueryService.save(governmentAccount)).thenReturn(governmentAccount);
        when(accountService.generateAccountNumber()).thenReturn(mockAccountNumber);

        GovernmentAccount createdAccount = governmentAccountService.create(request);

        assertEquals(governmentAccount.getGovernmentDepartment(), createdAccount.getGovernmentDepartment());
        assertEquals(mockAccountNumber, governmentAccount.getAccount().getAccountNumber());
        verify(governmentAccountQueryService, times(1)).save(governmentAccount);
    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        GovernmentAccount invalidAccount = new GovernmentAccount();

        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(governmentAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> governmentAccountService.create(request));

        assertEquals("invalid account", exception.getMessage());
        verify(governmentAccountQueryService, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        GovernmentAccount existingAccount = accountTestCases.getGovernmentAccountTestCase();
        String username = "username";
        String newEmail =  "newEmail@example.com";
        String newDepartment = "NewDepartment";

        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();
        request.setGovernmentDepartment(newDepartment);
        request.setEmail(newEmail);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(governmentAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doAnswer(inv -> {
            existingAccount.setGovernmentDepartment(request.getGovernmentDepartment());
            existingAccount.getAccount().setEmail(request.getEmail());
            return null;
        }).when(governmentAccountValidator).validateUpdate(request, existingAccount);
        when(governmentAccountQueryService.save(existingAccount)).thenReturn(existingAccount);

        GovernmentAccount updatedAccount = governmentAccountService.update(request);


        assertEquals(updatedAccount.getGovernmentDepartment(), request.getGovernmentDepartment());
        assertEquals(updatedAccount.getAccount().getEmail(), request.getEmail());
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        GovernmentAccount existingAccount = accountTestCases.getGovernmentAccountTestCase();
        String username = "username";

        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(governmentAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(governmentAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> governmentAccountService.update(request));

        assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(governmentAccountQueryService, never()).save(any());
    }
}
