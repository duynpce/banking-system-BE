package com.example.banking_system.card.unit;

import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.entity.CardPrivilegeCode;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.PersonalCardRepository;
import com.example.banking_system.card.service.domain.CardService;
import com.example.banking_system.card.service.domain.PersonalCardService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.validator.PersonalCardValidator;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class PersonalCardServiceUnitTest extends UnitTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Mock
    PersonalCardRepository personalCardRepository;

    @Mock
    PersonalCardValidator personalCardValidator;

    @Mock
    CardService cardService;

    @Mock
    AccountQueryService accountQueryService;

    @Mock
    CardPrivilegeQueryService cardPrivilegeQueryService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    PersonalCardService personalCardService;

    @Test
    public void createCardSuccess() {
        final String username = "username";
        final String cardNumber = "1234567890123456";

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        CardPrivilegeCode cardPrivilegeCode = new CardPrivilegeCode();
        cardPrivilegeCode.setCode("STANDARD");
        CardPrivilege privilege = new CardPrivilege();
        privilege.setCardPrivilegeCode(cardPrivilegeCode);

        CreatePersonalCardRequest request = cardTestCases.getCreatePersonalCardRequestTestCase();

        PersonalCard personalCard = new PersonalCard();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        doNothing().when(personalCardValidator).validateCreate(account);
        when(cardService.generateCardNumber()).thenReturn(cardNumber);
        when(cardPrivilegeQueryService.findByPrivilegeCode(request.getPrivilegeCode())).thenReturn(privilege);
        doNothing().when(cardService).updateExpirationDateOnCreate(any());
        when(personalCardRepository.save(any(PersonalCard.class))).thenReturn(personalCard);

        PersonalCard createdCard = personalCardService.create(request);

        Assertions.assertEquals(personalCard, createdCard);
        verify(personalCardValidator).validateCreate(account);
        verify(cardService).generateCardNumber();
        verify(cardPrivilegeQueryService).findByPrivilegeCode(request.getPrivilegeCode());
        verify(personalCardRepository, times(1)).save(any(PersonalCard.class));
    }

    @Test
    public void createCardFailure_InvalidAccount() {
        final String username = "username";

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        CreatePersonalCardRequest request = cardTestCases.getCreatePersonalCardRequestTestCase();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        doThrow(new ValidationException("invalid account")).when(personalCardValidator).validateCreate(account);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> personalCardService.create(request));

        Assertions.assertEquals("invalid account", exception.getMessage());
        verify(personalCardValidator).validateCreate(account);
        verify(cardService, never()).generateCardNumber();
        verify(personalCardRepository, never()).save(any());
    }
}
