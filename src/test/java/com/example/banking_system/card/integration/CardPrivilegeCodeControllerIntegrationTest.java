package com.example.banking_system.card.integration;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.card.controller.CardPrivilegeCodeController;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeCodeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        request.setCode("INVALID_CODE");
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

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetAllCardPrivilegeCodeSuccess() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        ResponseEntity<ResponseDto<List<GetCardPrivilegeCodeResponse>>> response = cardPrivilegeCodeController.getAll();
        ResponseDto<List<GetCardPrivilegeCodeResponse>> responseDto = response.getBody();
        List<GetCardPrivilegeCodeResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        cleanupCreatedCode(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetByPageCardPrivilegeCodeSuccess() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        ResponseEntity<ResponseDto<List<GetCardPrivilegeCodeResponse>>> response = cardPrivilegeCodeController.getByPage(0, 10);
        ResponseDto<List<GetCardPrivilegeCodeResponse>> responseDto = response.getBody();
        List<GetCardPrivilegeCodeResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        cleanupCreatedCode(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetByPageCardPrivilegeCodeInvalidPageFail() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cardPrivilegeCodeController.getByPage(-1, 10),
                "Should throw IllegalArgumentException when page is negative");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeCodeByIdSuccess() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        long id = cardPrivilegeCodeQueryService.findByCodeAndIsActive(createRequest.getCode()).getId();
        ResponseEntity<ResponseDto<GetCardPrivilegeCodeResponse>> response = cardPrivilegeCodeController.getById(id);
        ResponseDto<GetCardPrivilegeCodeResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(id, responseDto.getData().getId(), "Card privilege code id should match");

        cleanupCreatedCode(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeCodeByIdNotFoundFail() {
        Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeCodeController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeCodeByCodeAndIsActiveSuccess() {
        CreateCardPrivilegeCodeRequest createRequest = cardTestCases.getCreateCardPrivilegeCodeRequestTestCase();
        cardPrivilegeCodeController.create(createRequest);

        ResponseEntity<ResponseDto<GetCardPrivilegeCodeResponse>> response = cardPrivilegeCodeController.getByCodeAndIsActive(createRequest.getCode());
        ResponseDto<GetCardPrivilegeCodeResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(createRequest.getCode(), responseDto.getData().getCode(), "Card privilege code should match");

        cleanupCreatedCode(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeCodeByCodeAndIsActiveNotFoundFail() {
        Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeCodeController.getByCodeAndIsActive("NONEXISTENT_CODE"),
                "Should throw NotFoundException when code does not exist");
    }

    private void cleanupCreatedCode(String code) {
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(code);
    }
}
