package com.example.banking_system.account.unit;

import com.example.banking_system.account.TestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.GovernmentAccountRepository;
import com.example.banking_system.account.service.AccountService;
import com.example.banking_system.account.service.GovernmentAccountService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.GovernmentAccountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class GovernmentAccountServiceUnitTest extends UnitTest {

    private final TestCases testCases = TestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    GovernmentAccountRepository governmentAccountRepository;

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
        GovernmentAccount governmentAccount = testCases.getGovernmentAccountTestCase();
        final String hashedPassword = "hashedPassword";

        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(governmentAccount);
        doNothing().when(governmentAccountValidator).validateCreate(governmentAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(governmentAccountRepository.save(governmentAccount)).thenReturn(governmentAccount);

        GovernmentAccount createdAccount = governmentAccountService.create(request);

        Assertions.assertEquals(governmentAccount, createdAccount);
        verify(governmentAccountRepository, times(1)).save(governmentAccount);
    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        GovernmentAccount invalidAccount = new GovernmentAccount();

        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(governmentAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            governmentAccountService.create(request);
        });

        Assertions.assertEquals("invalid account", exception.getMessage());
        verify(governmentAccountRepository, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        GovernmentAccount existingAccount = testCases.getGovernmentAccountTestCase();
        String username = "username";

        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();
        request.setGovernmentDepartment("NewDepartment");
        request.setEmail("newemail@example.com");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.GOVERNMENT)).thenReturn(existingAccount);
        doNothing().when(governmentAccountValidator).validateUpdate(request, existingAccount);
        when(governmentAccountRepository.save(existingAccount)).thenReturn(existingAccount);

        GovernmentAccount updatedAccount = governmentAccountService.update(request);

        Assertions.assertEquals(existingAccount, updatedAccount);
        verify(governmentAccountValidator).validateUpdate(request, existingAccount);
        verify(governmentAccountRepository).save(existingAccount);
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        GovernmentAccount existingAccount = testCases.getGovernmentAccountTestCase();
        String username = "username";

        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.GOVERNMENT)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(governmentAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            governmentAccountService.update(request);
        });

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(governmentAccountRepository, never()).save(any());
    }
}
