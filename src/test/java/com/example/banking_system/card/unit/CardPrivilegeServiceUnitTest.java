package com.example.banking_system.card.unit;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.entity.CardPrivilegeCode;
import com.example.banking_system.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.card.service.domain.CardPrivilegeService;
import com.example.banking_system.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.validator.CardPrivilegeValidator;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class CardPrivilegeServiceUnitTest extends UnitTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Mock
    CardPrivilegeValidator cardPrivilegeValidator;

    @Mock
    CardPrivilegeMapper cardPrivilegeMapper;

    @Mock
    CardPrivilegeQueryService cardPrivilegeQueryService;

    @Mock
    CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @InjectMocks
    CardPrivilegeService cardPrivilegeService;

    @Test
    public void createCardPrivilegeSuccess() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();

        CardPrivilegeCode code = new CardPrivilegeCode();
        code.setCode(request.getCode());

        CardPrivilege cardPrivilege = new CardPrivilege();
        cardPrivilege.setCardPrivilegeCode(code);

        CardPrivilege savedCardPrivilege = new CardPrivilege();
        savedCardPrivilege.setCardPrivilegeCode(code);

        when(cardPrivilegeMapper.toEntity(request)).thenReturn(cardPrivilege);
        doNothing().when(cardPrivilegeValidator).validateCreate(cardPrivilege);
        when(cardPrivilegeCodeQueryService.findByCodeAndIsActive(request.getCode())).thenReturn(code);
        when(cardPrivilegeQueryService.save(cardPrivilege)).thenReturn(savedCardPrivilege);

        CardPrivilege result = cardPrivilegeService.create(request);

        Assertions.assertEquals(savedCardPrivilege, result);
        verify(cardPrivilegeMapper).toEntity(request);
        verify(cardPrivilegeValidator).validateCreate(cardPrivilege);
        verify(cardPrivilegeQueryService).save(cardPrivilege);
    }

    @Test
    public void createCardPrivilegeFailure_ValidationError() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();

        CardPrivilege cardPrivilege = new CardPrivilege();

        when(cardPrivilegeMapper.toEntity(request)).thenReturn(cardPrivilege);
        doThrow(new ValidationException("Privilege code already exists")).when(cardPrivilegeValidator).validateCreate(cardPrivilege);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class,
            () -> cardPrivilegeService.create(request));

        Assertions.assertEquals("Privilege code already exists", exception.getMessage());
        verify(cardPrivilegeMapper).toEntity(request);
        verify(cardPrivilegeValidator).validateCreate(cardPrivilege);
        verify(cardPrivilegeQueryService, never()).save(any());
    }

    @Test
    public void updateCardPrivilegeSuccess() {
        UpdateCardPrivilegeRequest request = cardTestCases.getUpdateCardPrivilegeRequestTestCase();

        CardPrivilegeCode code = new CardPrivilegeCode();
        code.setCode(request.getCode());

        CardPrivilege existingCardPrivilege = new CardPrivilege();
        existingCardPrivilege.setCardPrivilegeCode(code);

        CardPrivilege updatedCardPrivilege = new CardPrivilege();
        updatedCardPrivilege.setCardPrivilegeCode(code);

        when(cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getCode())).thenReturn(existingCardPrivilege);
        doNothing().when(cardPrivilegeValidator).validateUpdate(request, existingCardPrivilege);
        when(cardPrivilegeQueryService.save(existingCardPrivilege)).thenReturn(updatedCardPrivilege);

        CardPrivilege result = cardPrivilegeService.update(request);

        Assertions.assertEquals(updatedCardPrivilege, result);
        verify(cardPrivilegeQueryService).findByPrivilegeCodeAndIsActive(request.getCode());
        verify(cardPrivilegeValidator).validateUpdate(request, existingCardPrivilege);
        verify(cardPrivilegeQueryService).save(existingCardPrivilege);
    }

    @Test
    public void updateCardPrivilegeFailure_ValidationError() {
        UpdateCardPrivilegeRequest request = cardTestCases.getUpdateCardPrivilegeRequestTestCase();

        CardPrivilege existingCardPrivilege = new CardPrivilege();

        when(cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getCode())).thenReturn(existingCardPrivilege);
        doThrow(new ValidationException("Both fields cannot be null")).when(cardPrivilegeValidator).validateUpdate(request, existingCardPrivilege);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class,
            () -> cardPrivilegeService.update(request));

        Assertions.assertEquals("Both fields cannot be null", exception.getMessage());
        verify(cardPrivilegeQueryService).findByPrivilegeCodeAndIsActive(request.getCode());
        verify(cardPrivilegeValidator).validateUpdate(request, existingCardPrivilege);
        verify(cardPrivilegeQueryService, never()).save(any());
    }

}
