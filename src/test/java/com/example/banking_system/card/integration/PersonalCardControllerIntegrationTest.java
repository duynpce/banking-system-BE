package com.example.banking_system.card.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.account.controller.PersonalAccountController;
import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.controller.CardController;
import com.example.banking_system.card.controller.CardPrivilegeController;
import com.example.banking_system.card.controller.PersonalCardController;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PersonalCardControllerIntegrationTest extends IntegrationTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();
    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private PersonalCardController personalCardController;

    @Autowired
    private PersonalAccountController personalAccountController;


    @Autowired
    private CardPrivilegeController cardPrivilegeController;

    @Autowired
    private CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreatePersonalCard_Success() {
        // Create personal account
        CreatePersonalAccountRequest accountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(accountRequest);
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create personal card
        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();
        ResponseEntity<String> response = personalCardController.create(cardRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Personal card created successfully", response.getBody());


    }

    @Test
    public void testCreatePersonalCard_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();

        Assertions.assertThrows(UnauthorizedException.class,
            () -> personalCardController.create(cardRequest),
            "Should throw UnauthorizedException when not logged in");
    }

    @Test
    public void testCreatePersonalCard_InvalidPrivilegeCode_Failure() {
        // Create personal account
        CreatePersonalAccountRequest accountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(accountRequest);

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create card with invalid privilege code
        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();
        cardRequest.setPrivilegeCode("INVALID_CODE");

        Assertions.assertThrows(NotFoundException.class,
            () -> personalCardController.create(cardRequest),
            "Should throw ValidationException for invalid privilege code");

    }
}
