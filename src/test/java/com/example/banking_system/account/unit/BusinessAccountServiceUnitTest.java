package com.example.banking_system.account.unit;

import com.example.banking_system.account.TestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.UpdateBusinessAccountRequest;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.account.repository.BusinessAccountRepository;
import com.example.banking_system.account.service.AccountService;
import com.example.banking_system.account.service.BusinessAccountService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.BusinessAccountValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class BusinessAccountServiceUnitTest extends UnitTest {

    private final TestCases testCases = TestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    BusinessAccountRepository businessAccountRepository;

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
        BusinessAccount businessAccount = testCases.getBusinessAccountTestCase();
        final String hashedPassword = "hashedPassword";

        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(businessAccount);
        doNothing().when(businessAccountValidator).validateCreate(businessAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(businessAccountRepository.save(businessAccount)).thenReturn(businessAccount);

        BusinessAccount createdAccount = businessAccountService.create(request);

        Assertions.assertEquals(businessAccount, createdAccount);
        verify(businessAccountRepository, times(1)).save(businessAccount);

    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        BusinessAccount invalidAccount = new BusinessAccount();

        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(businessAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            businessAccountService.create(request);
        });

        Assertions.assertEquals("invalid account", exception.getMessage());
        verify(businessAccountRepository, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        BusinessAccount existingAccount = testCases.getBusinessAccountTestCase();
        String username = "username";

        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        request.setOrganizationName("NewOrganizationName");
        request.setEmail("newemail@example.com");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.BUSINESS)).thenReturn(existingAccount);
        doNothing().when(businessAccountValidator).validateUpdate(request, existingAccount);
        when(businessAccountRepository.save(existingAccount)).thenReturn(existingAccount);

        BusinessAccount updatedAccount = businessAccountService.update(request);

        Assertions.assertEquals(existingAccount, updatedAccount);
        verify(businessAccountValidator).validateUpdate(request, existingAccount);
        verify(businessAccountRepository).save(existingAccount);
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        BusinessAccount existingAccount = testCases.getBusinessAccountTestCase();
        String username = "username";

        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.BUSINESS)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(businessAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            businessAccountService.update(request);
        });

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(businessAccountRepository, never()).save(any());
    }

}
