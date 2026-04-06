package com.example.banking_system.card.integration;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.controller.CardPrivilegeController;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ConflictDataException;
import com.example.banking_system.common.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardPrivilegeControllerIntegrationTest extends IntegrationTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private CardPrivilegeController cardPrivilegeController;

    @Autowired
    private CardPrivilegeQueryService cardPrivilegeQueryService;

    @Test
    public void testCreateCardPrivilege_Success() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = cardPrivilegeController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        // Verify privilege was created
        assertTrue(cardPrivilegeQueryService.existsByCode(request.getCode()),
            "Card privilege should exist in database");

        cleanupCreatedCardPrivilege(request.getCode());
    }

    @Test
    public void testCreateCardPrivilege_DuplicateCode_Failure() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();

        // Create first privilege
        cardPrivilegeController.create(request);

        // Try to create duplicate
        Assertions.assertThrows(ConflictDataException.class,
            () -> cardPrivilegeController.create(request),
            "Should throw ValidationException for duplicate privilege code");

        cleanupCreatedCardPrivilege(request.getCode());
    }

    @Test
    public void testUpdateCardPrivilege_Success() {
        // Create privilege first
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        // Update privilege
        UpdateCardPrivilegeRequest updateRequest = cardTestCases.getUpdateCardPrivilegeRequestTestCase();
        updateRequest.setCode(createRequest.getCode()); // Use the same code

        ResponseEntity<ResponseDto<String>> response = cardPrivilegeController.update(updateRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        cleanupCreatedCardPrivilege(createRequest.getCode());
    }

    @Test
    public void testUpdateCardPrivilege_NotFound_Failure() {
        UpdateCardPrivilegeRequest request = cardTestCases.getUpdateCardPrivilegeRequestTestCase();
        request.setCode("NONEXISTENT_CODE");

        Assertions.assertThrows(NotFoundException.class,
            () -> cardPrivilegeController.update(request),
            "Should throw ValidationException when privilege code not found");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetAllCardPrivilege_Success() {
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> response = cardPrivilegeController.getAll();
        ResponseDto<List<GetCardPrivilegeResponse>> responseDto = response.getBody();
        List<GetCardPrivilegeResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        cleanupCreatedCardPrivilege(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetByPageCardPrivilege_Success() {
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        ResponseEntity<ResponseDto<List<GetCardPrivilegeResponse>>> response = cardPrivilegeController.getByPage(0, 10);
        ResponseDto<List<GetCardPrivilegeResponse>> responseDto = response.getBody();
        List<GetCardPrivilegeResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        cleanupCreatedCardPrivilege(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetByPageCardPrivilege_InvalidPage_Failure() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cardPrivilegeController.getByPage(-1, 10),
                "Should throw IllegalArgumentException when page is negative");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeById_Success() {
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        long id = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(createRequest.getCode()).getId();
        ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> response = cardPrivilegeController.getById(id);
        ResponseDto<GetCardPrivilegeResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(id, responseDto.getData().getId(), "Card privilege id should match");

        cleanupCreatedCardPrivilege(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeById_NotFound_Failure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeByCodeAndIsActive_Success() {
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        ResponseEntity<ResponseDto<GetCardPrivilegeResponse>> response = cardPrivilegeController.getByCodeAndIsActive(createRequest.getCode());
        ResponseDto<GetCardPrivilegeResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(createRequest.getCode(), responseDto.getData().getPrivilegeCode(), "Card privilege code should match");

        cleanupCreatedCardPrivilege(createRequest.getCode());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetCardPrivilegeByCodeAndIsActive_NotFound_Failure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> cardPrivilegeController.getByCodeAndIsActive("NONEXISTENT_CODE"),
                "Should throw NotFoundException when code does not exist");
    }

    @Test
    public void testDeleteCardPrivilege_Success() {
        // Create privilege first
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeController.create(createRequest);

        // Delete privilege
        ResponseEntity<ResponseDto<String>> response = cardPrivilegeController.deleteCardPrivilegeAndIsActive(createRequest.getCode());
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
    }

    @Test
    public void testDeleteCardPrivilege_NotFound_Failure() {
        String nonExistentCode = "NONEXISTENT_CODE";

        Assertions.assertThrows(NotFoundException.class,
            () -> cardPrivilegeController.deleteCardPrivilegeAndIsActive(nonExistentCode),
            "Should throw ValidationException when trying to delete non-existent privilege");
    }

    private void cleanupCreatedCardPrivilege(String code) {
        cardPrivilegeQueryService.deleteByPrivilegeCode(code);
    }

}
