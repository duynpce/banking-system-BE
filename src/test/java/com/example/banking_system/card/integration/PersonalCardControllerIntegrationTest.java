package com.example.banking_system.card.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.controller.PersonalAccountController;
import com.example.banking_system.domain.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.controller.CardPrivilegeController;
import com.example.banking_system.domain.card.controller.PersonalCardController;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreatePersonalCard_Success() {
        // Create personal account
        CreatePersonalAccountRequest accountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        CreateCardPrivilegeRequest createCardPrivilegeRequest = cardTestCases.getCreateCardPrivilegeRequestTestCase();
        createCardPrivilegeRequest.setAccountType(accountRequest.getType());
        personalAccountController.create(accountRequest);
        cardPrivilegeController.create(createCardPrivilegeRequest);


        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create personal card
        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();
        ResponseEntity<ResponseDto<String>> response = personalCardController.create(cardRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");


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
