package com.example.banking_system.card.integration;

import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.controller.CardPrivilegeController;
import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.exception.ConflictDataException;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CardPrivilegeControllerIntegrationTest extends IntegrationTest {

    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private CardPrivilegeController cardPrivilegeController;

    @Autowired
    private CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @Test
    public void testCreateCardPrivilege_Success() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());

        ResponseEntity<String> response = cardPrivilegeController.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Card privilege created successfully", response.getBody());

        // Verify privilege was created
        assertTrue(cardPrivilegeCodeQueryService.existsByCode(request.getCode()),
            "Card privilege should exist in database");

        // Clean up
        cardPrivilegeController.deleteCardPrivilege(request.getCode());
    }

    @Test
    public void testCreateCardPrivilege_DuplicateCode_Failure() {
        CreateCardPrivilegeRequest request = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());

        // Create first privilege
        cardPrivilegeController.create(request);

        // Try to create duplicate
        Assertions.assertThrows(ConflictDataException.class,
            () -> cardPrivilegeController.create(request),
            "Should throw ValidationException for duplicate privilege code");

        // Clean up
        cardPrivilegeController.deleteCardPrivilege(request.getCode());
    }

    @Test
    public void testUpdateCardPrivilege_Success() {
        // Create privilege first
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(createRequest);

        // Update privilege
        UpdateCardPrivilegeRequest updateRequest = cardTestCases.getUpdateCardPrivilegeRequestTestCase();
        updateRequest.setCode(createRequest.getCode()); // Use the same code

        ResponseEntity<String> response = cardPrivilegeController.update(updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Card privilege updated successfully", response.getBody());

        // Clean up
        cardPrivilegeController.deleteCardPrivilege(createRequest.getCode());
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
    public void testDeleteCardPrivilege_Success() {
        // Create privilege first
        CreateCardPrivilegeRequest createRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(createRequest);

        // Delete privilege
        ResponseEntity<Void> response = cardPrivilegeController.deleteCardPrivilege(createRequest.getCode());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Response status should be NO_CONTENT");
    }

    @Test
    public void testDeleteCardPrivilege_NotFound_Failure() {
        String nonExistentCode = "NONEXISTENT_CODE";

        Assertions.assertThrows(NotFoundException.class,
            () -> cardPrivilegeController.deleteCardPrivilege(nonExistentCode),
            "Should throw ValidationException when trying to delete non-existent privilege");
    }
}
