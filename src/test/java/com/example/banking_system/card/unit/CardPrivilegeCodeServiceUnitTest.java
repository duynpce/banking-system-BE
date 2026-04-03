package com.example.banking_system.card.unit;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
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
}
