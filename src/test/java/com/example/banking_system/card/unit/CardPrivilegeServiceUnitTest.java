package com.example.banking_system.card.unit;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.domain.card.validator.CardPrivilegeValidator;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.Mockito.*;

public class CardPrivilegeServiceUnitTest extends UnitTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Mock
    CardPrivilegeValidator cardPrivilegeValidator;

    @Mock
    CardPrivilegeMapper cardPrivilegeMapper;

    @Mock
    CardPrivilegeQueryService cardPrivilegeQueryService;

    @InjectMocks
    CardPrivilegeService cardPrivilegeService;

    @Test
    public void createCardPrivilegeSuccess() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();

        CardPrivilege cardPrivilege = new CardPrivilege();
        cardPrivilege.setCode(request.getCode());

        CardPrivilege savedCardPrivilege = new CardPrivilege();
        savedCardPrivilege.setCode(request.getCode());

        when(cardPrivilegeMapper.toEntity(request)).thenReturn(cardPrivilege);
        doNothing().when(cardPrivilegeValidator).validateCreate(cardPrivilege);
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
        
        CardPrivilege existingCardPrivilege = new CardPrivilege();
        existingCardPrivilege.setCode(request.getCode());

        CardPrivilege updatedCardPrivilege = new CardPrivilege();
        updatedCardPrivilege.setCode(request.getCode());

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

    @Test
    public void getCardPrivilegeByIdSuccess() {
        CardPrivilege cardPrivilege = cardTestCases.getCardPrivilegeTestCase();
        cardPrivilege.setId(1L);
        GetCardPrivilegeResponse expectedResponse = cardTestCases.getCardPrivilegeResponseTestCase();

        when(cardPrivilegeQueryService.findById(1L)).thenReturn(cardPrivilege);
        when(cardPrivilegeMapper.toDto(cardPrivilege)).thenReturn(expectedResponse);

        GetCardPrivilegeResponse result = cardPrivilegeService.getById(1L);

        Assertions.assertEquals(expectedResponse, result);
        verify(cardPrivilegeQueryService).findById(1L);
        verify(cardPrivilegeMapper).toDto(cardPrivilege);
    }

    @Test
    public void getCardPrivilegeByIdFailure_NotFound() {
        when(cardPrivilegeQueryService.findById(999L))
                .thenThrow(new NotFoundException("Card privilege not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeService.getById(999L));

        Assertions.assertEquals("Card privilege not found with id: 999", exception.getMessage());
        verify(cardPrivilegeQueryService).findById(999L);
        verify(cardPrivilegeMapper, never()).toDto(any());
    }

    @Test
    public void getCardPrivilegeByCodeAndIsActiveSuccess() {
        CardPrivilege cardPrivilege = cardTestCases.getCardPrivilegeTestCase();
        GetCardPrivilegeResponse expectedResponse = cardTestCases.getCardPrivilegeResponseTestCase();

        when(cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive("CODE")).thenReturn(cardPrivilege);
        when(cardPrivilegeMapper.toDto(cardPrivilege)).thenReturn(expectedResponse);

        GetCardPrivilegeResponse result = cardPrivilegeService.getByCodeAndIsActive("code");

        Assertions.assertEquals(expectedResponse, result);
        verify(cardPrivilegeQueryService).findByPrivilegeCodeAndIsActive("CODE");
        verify(cardPrivilegeMapper).toDto(cardPrivilege);
    }

    @Test
    public void getCardPrivilegeByCodeAndIsActiveFailure_NotFound() {
        when(cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive("MISSING"))
                .thenThrow(new NotFoundException("Card privilege not found with code: MISSING"));

        RuntimeException exception = Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeService.getByCodeAndIsActive("missing"));

        Assertions.assertEquals("Card privilege not found with code: MISSING", exception.getMessage());
        verify(cardPrivilegeQueryService).findByPrivilegeCodeAndIsActive("MISSING");
        verify(cardPrivilegeMapper, never()).toDto(any());
    }

    @Test
    public void getAllCardPrivilegeSuccess() {
        CardPrivilege cardPrivilege = cardTestCases.getCardPrivilegeTestCase();
        GetCardPrivilegeResponse response = cardTestCases.getCardPrivilegeResponseTestCase();

        when(cardPrivilegeQueryService.findAll()).thenReturn(List.of(cardPrivilege));
        when(cardPrivilegeMapper.toDtoList(List.of(cardPrivilege))).thenReturn(List.of(response));

        List<GetCardPrivilegeResponse> result = cardPrivilegeService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(cardPrivilegeQueryService).findAll();
        verify(cardPrivilegeMapper).toDtoList(List.of(cardPrivilege));
    }

    @Test
    public void getByPageCardPrivilegeSuccess() {
        CardPrivilege cardPrivilege = cardTestCases.getCardPrivilegeTestCase();
        GetCardPrivilegeResponse response = cardTestCases.getCardPrivilegeResponseTestCase();
        Page<CardPrivilege> page = new PageImpl<>(List.of(cardPrivilege));

        when(cardPrivilegeQueryService.findAllWithPagination(0, 10)).thenReturn(page);
        when(cardPrivilegeMapper.toDtoList(List.of(cardPrivilege))).thenReturn(List.of(response));

        List<GetCardPrivilegeResponse> result = cardPrivilegeService.getByPage(0, 10);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(cardPrivilegeQueryService).findAllWithPagination(0, 10);
        verify(cardPrivilegeMapper).toDtoList(List.of(cardPrivilege));
    }

    @Test
    public void getByPageCardPrivilegeFailure_InvalidPagination() {
        when(cardPrivilegeQueryService.findAllWithPagination(-1, 10))
                .thenThrow(new IllegalArgumentException("Page index must not be less than zero"));

        RuntimeException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cardPrivilegeService.getByPage(-1, 10));

        Assertions.assertEquals("Page index must not be less than zero", exception.getMessage());
        verify(cardPrivilegeQueryService).findAllWithPagination(-1, 10);
        verify(cardPrivilegeMapper, never()).toDtoList(any());
    }

}
