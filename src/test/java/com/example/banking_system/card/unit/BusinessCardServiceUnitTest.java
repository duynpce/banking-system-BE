package com.example.banking_system.card.unit;

import com.example.banking_system.account.entity.Account;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.entity.CardPrivilegeCode;
import com.example.banking_system.card.repository.BusinessCardRepository;
import com.example.banking_system.card.service.domain.BusinessCardService;
import com.example.banking_system.card.service.domain.CardService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.validator.BusinessCardValidator;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class BusinessCardServiceUnitTest extends UnitTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Mock
    BusinessCardRepository businessCardRepository;

    @Mock
    BusinessCardValidator businessCardValidator;

    @Mock
    CardService cardService;

    @Mock
    AccountQueryService accountQueryService;

    @Mock
    CardPrivilegeQueryService cardPrivilegeQueryService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    BusinessCardService businessCardService;

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

        CreateBusinessCardRequest request = cardTestCases.getCreateBusinessCardRequestTestCase();

        BusinessCard businessCard = new BusinessCard();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        doNothing().when(businessCardValidator).validateCreate(account);
        when(cardService.generateCardNumber()).thenReturn(cardNumber);
        when(cardPrivilegeQueryService.findByPrivilegeCode(request.getPrivilegeCode())).thenReturn(privilege);
        doNothing().when(cardService).updateExpirationDateOnCreate(any());
        when(businessCardRepository.save(any(BusinessCard.class))).thenReturn(businessCard);

        BusinessCard createdCard = businessCardService.create(request);

        Assertions.assertEquals(businessCard, createdCard);
        verify(businessCardValidator).validateCreate(account);
        verify(cardService).generateCardNumber();
        verify(cardPrivilegeQueryService).findByPrivilegeCode(request.getPrivilegeCode());
        verify(businessCardRepository, times(1)).save(any(BusinessCard.class));
    }

    @Test
    public void createCardFailure_InvalidAccount() {
        final String username = "username";

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        CreateBusinessCardRequest request = cardTestCases.getCreateBusinessCardRequestTestCase();

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        doThrow(new ValidationException("invalid account")).when(businessCardValidator).validateCreate(account);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class, () -> businessCardService.create(request));

        Assertions.assertEquals("invalid account", exception.getMessage());
        verify(businessCardValidator).validateCreate(account);
        verify(cardService, never()).generateCardNumber();
        verify(businessCardRepository, never()).save(any());
    }
}
