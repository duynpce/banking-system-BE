package com.example.banking_system.card.integration;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.card.controller.CardPrivilegeCodeController;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardPrivilegeCodeControllerIntegrationTest extends IntegrationTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private CardPrivilegeCodeController cardPrivilegeCodeController;

    @Autowired
    private CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @Test
    public void testCreateCardPrivilegeCodeSuccess() {
        CreateCardPrivilegeCodeRequest request = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = cardPrivilegeCodeController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertTrue(cardPrivilegeCodeQueryService.existsByCode(request.getCode()), "Card privilege code should exist in database");
    }

    @Test
    public void testCreateCardPrivilegeCodeFailValidation() {
        CreateCardPrivilegeCodeRequest request = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        request.setCode("INVALID");
        request.setEffectiveFrom(LocalDate.now().plusDays(10));
        request.setEffectiveTo(LocalDate.now().plusDays(5));

        Assertions.assertThrows(ValidationException.class,
                () -> cardPrivilegeCodeController.create(request),
                "Should throw ValidationException for invalid effective date range");
    }

    @Test
    public void testUpdateCardPrivilegeCodeSuccess() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        UpdateCardPrivilegeCodeRequest updateRequest = cardTestCases.getUpdateCardPrivilegeCodeRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = cardPrivilegeCodeController.update(updateRequest);
        ResponseDto<String> responseDto = response.getBody();

        CardPrivilegeCode updated = cardPrivilegeCodeQueryService.findByCodeAndDate(updateRequest.getCode(), LocalDate.now().plusDays(1));

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertEquals(updateRequest.getExpirationYears(), updated.getExpirationYears(), "Expiration years should be updated");
    }

    @Test
    public void testUpdateCardPrivilegeCodeFail_allFieldNull() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        UpdateCardPrivilegeCodeRequest updateRequest = new UpdateCardPrivilegeCodeRequest();
        updateRequest.setCode(createRequest.getCode());

        Assertions.assertThrows(ValidationException.class,
                () -> cardPrivilegeCodeController.update(updateRequest),
                "Should throw ValidationException when no updatable fields are provided");
    }
}
