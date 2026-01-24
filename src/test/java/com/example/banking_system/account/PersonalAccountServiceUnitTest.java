package com.example.banking_system.account;

import com.example.banking_system.UnitTest;
import com.example.banking_system.constant.AccountType;
import com.example.banking_system.dto.account.CreatePersonalAccountRequest;
import com.example.banking_system.dto.account.UpdatePersonalAccountRequest;
import com.example.banking_system.entity.account.PersonalAccount;
import com.example.banking_system.exception.ValidationException;
import com.example.banking_system.mapper.AccountMapper;
import com.example.banking_system.repository.account.PersonalAccountRepository;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.service.account.PersonalAccountService;
import com.example.banking_system.utility.JwtUtil;
import com.example.banking_system.validator.account.PersonalAccountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PersonalAccountServiceUnitTest extends UnitTest {

    private final TestCases testCases = TestCases.getInstance();

    @Mock
    AccountMapper accountMapper;

    @Mock
    PersonalAccountRepository personalAccountRepository;

    @Mock
    PersonalAccountValidator personalAccountValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccountService accountService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    PersonalAccountService personalAccountService;

//    @Test
//    public void testCi_shouldFail(){
//        assertTrue(false);
//    }

    @Test
    public void createAccountSuccess() {
        PersonalAccount personalAccount = testCases.getPersonalAccountTestCase();
        final String hashedPassword = "hashedPassword";

        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(personalAccount);
        doNothing().when(personalAccountValidator).validateCreate(personalAccount);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(hashedPassword);
        when(personalAccountRepository.save(personalAccount)).thenReturn(personalAccount);

        PersonalAccount createdAccount = personalAccountService.create(request);

        assertEquals(personalAccount, createdAccount);
        verify(personalAccountRepository, times(1)).save(personalAccount);
    }

    @Test
    public void createAccountFailure_InvalidAccount() {
        PersonalAccount invalidAccount = new PersonalAccount();

        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();

        when(accountMapper.toEntity(request)).thenReturn(invalidAccount);
        doThrow(new ValidationException("invalid account")).when(personalAccountValidator).validateCreate(invalidAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            personalAccountService.create(request);
        });

        assertEquals("invalid account", exception.getMessage());
        verify(personalAccountRepository, never()).save(any());
    }

    @Test
    public void updateAccountSuccess() {
        PersonalAccount existingAccount = testCases.getPersonalAccountTestCase();
        String username = "username";

        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();
        request.setFullName("NewFullName");
        request.setEmail("newemail@example.com");

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.PERSONAL)).thenReturn(existingAccount);
        doNothing().when(personalAccountValidator).validateUpdate(request, existingAccount);
        when(personalAccountRepository.save(existingAccount)).thenReturn(existingAccount);

        PersonalAccount updatedAccount = personalAccountService.update(request);

        assertEquals(existingAccount, updatedAccount);
        verify(personalAccountValidator).validateUpdate(request, existingAccount);
        verify(personalAccountRepository).save(existingAccount);
    }

    @Test
    public void updateAccountFailure_InvalidInput() {
        PersonalAccount existingAccount = testCases.getPersonalAccountTestCase();
        String username = "username";

        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountService.findByUsernameAndType(username, AccountType.PERSONAL)).thenReturn(existingAccount);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(personalAccountValidator).validateUpdate(request, existingAccount);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> {
            personalAccountService.update(request);
        });

        assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(personalAccountRepository, never()).save(any());
    }
}

