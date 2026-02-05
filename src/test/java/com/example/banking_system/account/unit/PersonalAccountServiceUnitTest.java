package com.example.banking_system.account.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.account.service.domain.PersonalAccountService;
import com.example.banking_system.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.account.dto.UpdatePersonalAccountRequest;
import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.account.mapper.AccountMapper;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.account.validator.PersonalAccountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonalAccountServiceUnitTest extends UnitTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    PersonalAccountQueryService personalAccountQueryService;

    @Mock
    PersonalAccountValidator personalAccountValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    PersonalAccountService personalAccountService;

    @Test
    public void createAccountSuccess() {
        PersonalAccount personalAccount = accountTestCases.getPersonalAccountTestCase();
        final String hashedPassword = "hashedPassword";

        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(personalAccount);
        doNothing().when(personalAccountValidator).validateCreate(personalAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(personalAccountQueryService.save(personalAccount)).thenReturn(personalAccount);

        PersonalAccount createdAccount = personalAccountService.create(request);

        assertEquals(personalAccount, createdAccount);
        verify(personalAccountQueryService, times(1)).save(personalAccount);
    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        PersonalAccount invalidAccount = new PersonalAccount();

        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(personalAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> personalAccountService.create(request));

        assertEquals("invalid account", exception.getMessage());
        verify(personalAccountQueryService, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        PersonalAccount existingAccount = accountTestCases.getPersonalAccountTestCase();
        String username = "username";

        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();
        request.setFullName("NewFullName");
        request.setEmail("newemail@example.com");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(personalAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doNothing().when(personalAccountValidator).validateUpdate(request, existingAccount);
        when(personalAccountQueryService.save(existingAccount)).thenReturn(existingAccount);

        PersonalAccount updatedAccount = personalAccountService.update(request);

        assertEquals(existingAccount, updatedAccount);
        verify(personalAccountValidator).validateUpdate(request, existingAccount);
        verify(personalAccountQueryService).save(existingAccount);
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        PersonalAccount existingAccount = accountTestCases.getPersonalAccountTestCase();
        String username = "username";

        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(personalAccountQueryService.findByUsername(username)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(personalAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> personalAccountService.update(request));

        assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(personalAccountQueryService, never()).save(any());
    }
}
