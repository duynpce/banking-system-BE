package com.example.banking_system.card.unit;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeCodeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.mapper.CardPrivilegeCodeMapper;
import com.example.banking_system.domain.card.service.domain.CardPrivilegeCodeService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.domain.card.validator.CardPrivilegeCodeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardPrivilegeCodeServiceUnitTest extends UnitTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Mock
    CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @Mock
    CardPrivilegeCodeMapper cardPrivilegeCodeMapper;

    @Mock
    CardPrivilegeCodeValidator cardPrivilegeCodeValidator;

    @InjectMocks
    CardPrivilegeCodeService cardPrivilegeCodeService;

    @Test
    public void createCardPrivilegeCodeSuccess() {
        CreateCardPrivilegeCodeRequest request = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        CardPrivilegeCode savedCardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();

        when(cardPrivilegeCodeMapper.toEntity(request)).thenReturn(cardPrivilegeCode);
        doNothing().when(cardPrivilegeCodeValidator).validateCreate(cardPrivilegeCode);
        when(cardPrivilegeCodeQueryService.save(cardPrivilegeCode)).thenReturn(savedCardPrivilegeCode);

        CardPrivilegeCode result = cardPrivilegeCodeService.create(request);

        Assertions.assertEquals(savedCardPrivilegeCode, result);
        verify(cardPrivilegeCodeMapper).toEntity(request);
        verify(cardPrivilegeCodeValidator).validateCreate(cardPrivilegeCode);
        verify(cardPrivilegeCodeQueryService).save(cardPrivilegeCode);
    }

    @Test
    public void createCardPrivilegeCodeFailureValidationError() {
        CreateCardPrivilegeCodeRequest request = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();

        when(cardPrivilegeCodeMapper.toEntity(request)).thenReturn(cardPrivilegeCode);
        doThrow(new ValidationException("An overlapping card privilege code already exists"))
                .when(cardPrivilegeCodeValidator).validateCreate(cardPrivilegeCode);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class,
                () -> cardPrivilegeCodeService.create(request));

        Assertions.assertEquals("An overlapping card privilege code already exists", exception.getMessage());
        verify(cardPrivilegeCodeMapper).toEntity(request);
        verify(cardPrivilegeCodeValidator).validateCreate(cardPrivilegeCode);
        verify(cardPrivilegeCodeQueryService, never()).save(any());
    }

    @Test
    public void updateCardPrivilegeCodeSuccess() {
        UpdateCardPrivilegeCodeRequest request = cardTestCases.getUpdateCardPrivilegeCodeRequestTestCase();

        CardPrivilegeCode existingCardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        CardPrivilegeCode updatedCardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        updatedCardPrivilegeCode.setExpirationYears(request.getExpirationYears());

        when(cardPrivilegeCodeQueryService.findByCodeAndIsActive("CODE")).thenReturn(existingCardPrivilegeCode);
        doNothing().when(cardPrivilegeCodeValidator).validateUpdate(request, existingCardPrivilegeCode);
        when(cardPrivilegeCodeQueryService.save(existingCardPrivilegeCode)).thenReturn(updatedCardPrivilegeCode);

        CardPrivilegeCode result = cardPrivilegeCodeService.update(request);

        Assertions.assertEquals(updatedCardPrivilegeCode, result);
        verify(cardPrivilegeCodeQueryService).findByCodeAndIsActive("CODE");
        verify(cardPrivilegeCodeValidator).validateUpdate(request, existingCardPrivilegeCode);
        verify(cardPrivilegeCodeQueryService).save(existingCardPrivilegeCode);
    }

    @Test
    public void updateCardPrivilegeCodeFailureValidationError() {
        UpdateCardPrivilegeCodeRequest request = cardTestCases.getUpdateCardPrivilegeCodeRequestTestCase();

        CardPrivilegeCode existingCardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();

        when(cardPrivilegeCodeQueryService.findByCodeAndIsActive(request.getCode())).thenReturn(existingCardPrivilegeCode);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(cardPrivilegeCodeValidator).validateUpdate(request, existingCardPrivilegeCode);

        RuntimeException exception = Assertions.assertThrows(ValidationException.class,
                () -> cardPrivilegeCodeService.update(request));

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(cardPrivilegeCodeQueryService).findByCodeAndIsActive(request.getCode());
        verify(cardPrivilegeCodeValidator).validateUpdate(request, existingCardPrivilegeCode);
        verify(cardPrivilegeCodeQueryService, never()).save(any());
    }

    @Test
    public void getCardPrivilegeCodeByIdSuccess() {
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        GetCardPrivilegeCodeResponse expectedResponse = cardTestCases.getCardPrivilegeCodeResponseTestCase();

        when(cardPrivilegeCodeQueryService.findById(1L)).thenReturn(cardPrivilegeCode);
        when(cardPrivilegeCodeMapper.toDto(cardPrivilegeCode)).thenReturn(expectedResponse);

        GetCardPrivilegeCodeResponse result = cardPrivilegeCodeService.getById(1L);

        Assertions.assertEquals(expectedResponse, result);
        verify(cardPrivilegeCodeQueryService).findById(1L);
        verify(cardPrivilegeCodeMapper).toDto(cardPrivilegeCode);
    }

    @Test
    public void getCardPrivilegeCodeByIdFailure_NotFound() {
        when(cardPrivilegeCodeQueryService.findById(999L))
                .thenThrow(new NotFoundException("Card privilege code not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeCodeService.getById(999L));

        Assertions.assertEquals("Card privilege code not found with id: 999", exception.getMessage());
        verify(cardPrivilegeCodeQueryService).findById(999L);
        verify(cardPrivilegeCodeMapper, never()).toDto(any());
    }

    @Test
    public void getCardPrivilegeCodeByCodeAndIsActiveSuccess() {
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        GetCardPrivilegeCodeResponse expectedResponse = cardTestCases.getCardPrivilegeCodeResponseTestCase();

        when(cardPrivilegeCodeQueryService.findByCodeAndIsActive("CODE")).thenReturn(cardPrivilegeCode);
        when(cardPrivilegeCodeMapper.toDto(cardPrivilegeCode)).thenReturn(expectedResponse);

        GetCardPrivilegeCodeResponse result = cardPrivilegeCodeService.getByCodeAndIsActive("code");

        Assertions.assertEquals(expectedResponse, result);
        verify(cardPrivilegeCodeQueryService).findByCodeAndIsActive("CODE");
        verify(cardPrivilegeCodeMapper).toDto(cardPrivilegeCode);
    }

    @Test
    public void getCardPrivilegeCodeByCodeAndIsActiveFailure_NotFound() {
        when(cardPrivilegeCodeQueryService.findByCodeAndIsActive("MISSING"))
                .thenThrow(new NotFoundException("no active code with code: MISSING"));

        RuntimeException exception = Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeCodeService.getByCodeAndIsActive("missing"));

        Assertions.assertEquals("no active code with code: MISSING", exception.getMessage());
        verify(cardPrivilegeCodeQueryService).findByCodeAndIsActive("MISSING");
        verify(cardPrivilegeCodeMapper, never()).toDto(any());
    }

    @Test
    public void getAllCardPrivilegeCodeSuccess() {
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        GetCardPrivilegeCodeResponse response = cardTestCases.getCardPrivilegeCodeResponseTestCase();

        when(cardPrivilegeCodeQueryService.findAll()).thenReturn(List.of(cardPrivilegeCode));
        when(cardPrivilegeCodeMapper.toDtoList(List.of(cardPrivilegeCode))).thenReturn(List.of(response));

        List<GetCardPrivilegeCodeResponse> result = cardPrivilegeCodeService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(cardPrivilegeCodeQueryService).findAll();
        verify(cardPrivilegeCodeMapper).toDtoList(List.of(cardPrivilegeCode));
    }

    @Test
    public void getByPageCardPrivilegeCodeSuccess() {
        CardPrivilegeCode cardPrivilegeCode = cardTestCases.getCardPrivilegeCodeTestCase();
        GetCardPrivilegeCodeResponse response = cardTestCases.getCardPrivilegeCodeResponseTestCase();
        Page<CardPrivilegeCode> page = new PageImpl<>(List.of(cardPrivilegeCode));

        when(cardPrivilegeCodeQueryService.findAllWithPagination(0, 10)).thenReturn(page);
        when(cardPrivilegeCodeMapper.toDtoList(List.of(cardPrivilegeCode))).thenReturn(List.of(response));

        List<GetCardPrivilegeCodeResponse> result = cardPrivilegeCodeService.getByPage(0, 10);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(cardPrivilegeCodeQueryService).findAllWithPagination(0, 10);
        verify(cardPrivilegeCodeMapper).toDtoList(List.of(cardPrivilegeCode));
    }

    @Test
    public void getByPageCardPrivilegeCodeFailure_InvalidPagination() {
        when(cardPrivilegeCodeQueryService.findAllWithPagination(-1, 10))
                .thenThrow(new IllegalArgumentException("Page index must not be less than zero"));

        RuntimeException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cardPrivilegeCodeService.getByPage(-1, 10));

        Assertions.assertEquals("Page index must not be less than zero", exception.getMessage());
        verify(cardPrivilegeCodeQueryService).findAllWithPagination(-1, 10);
        verify(cardPrivilegeCodeMapper, never()).toDtoList(any());
    }
}
