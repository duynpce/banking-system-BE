package com.example.banking_system.card.unit;

import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.entity.PersonalAccount;
import com.example.banking_system.domain.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.domain.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.entity.PersonalCard;
import com.example.banking_system.domain.card.repository.PersonalCardRepository;
import com.example.banking_system.domain.card.service.domain.CardService;
import com.example.banking_system.domain.card.service.domain.PersonalCardService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.domain.card.validator.PersonalCardValidator;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.NotFoundException;
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
    PersonalAccountQueryService personalAccountQueryService;

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
        PersonalAccount personalAccount = new PersonalAccount();
        personalAccount.setAccount(account);


        CardPrivilege  cardPrivilege = cardTestCases.getCardPrivilegeTestCase();
        CreatePersonalCardRequest request = cardTestCases.getCreatePersonalCardRequestTestCase();

        PersonalCard personalCard = new PersonalCard();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(personalAccountQueryService.findByUsername(username)).thenReturn(personalAccount);
        doNothing().when(personalCardValidator).validateCreate(account);
        when(cardService.generateCardNumber()).thenReturn(cardNumber);
        when(cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getPrivilegeCode())).thenReturn(cardPrivilege);
        doNothing().when(cardService).updateExpirationDateOnCreate(any());
        when(personalCardRepository.save(any(PersonalCard.class))).thenReturn(personalCard);

        PersonalCard createdCard = personalCardService.create(request);

        Assertions.assertEquals(personalCard, createdCard);
        verify(personalCardValidator).validateCreate(account);
        verify(cardService).generateCardNumber();
        verify(cardPrivilegeQueryService).findByPrivilegeCodeAndIsActive(request.getPrivilegeCode());
        verify(personalCardRepository, times(1)).save(any(PersonalCard.class));
    }

    @Test
    public void createCardFailure_NotFoundAccount() {
        final String username = "username";

        CreatePersonalCardRequest request = cardTestCases.getCreatePersonalCardRequestTestCase();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(personalAccountQueryService.findByUsername(username)).thenThrow(new NotFoundException("Personal account not found with username: " + username));

        RuntimeException exception = Assertions.assertThrows(NotFoundException.class, () -> personalCardService.create(request));

        Assertions.assertEquals("Personal account not found with username: " + username, exception.getMessage());
        verify(cardService, never()).generateCardNumber();
        verify(personalCardRepository, never()).save(any());
    }
}
