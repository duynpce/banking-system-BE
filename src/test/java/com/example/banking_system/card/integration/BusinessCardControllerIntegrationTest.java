package com.example.banking_system.card.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.controller.BusinessAccountController;
import com.example.banking_system.domain.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.controller.BusinessCardController;
import com.example.banking_system.domain.card.controller.CardPrivilegeController;
import com.example.banking_system.domain.card.dto.CreateBusinessCardRequest;
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

public class BusinessCardControllerIntegrationTest extends IntegrationTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();
    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private BusinessCardController businessCardController;

    @Autowired
    private BusinessAccountController businessAccountController;

    @Autowired
    private CardPrivilegeController cardPrivilegeController;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateBusinessCard_Success() {
        // Create business account
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        ResponseEntity<ResponseDto<String>> response = businessCardController.create(cardRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");


    }

    @Test
    public void testCreateBusinessCard_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();

        Assertions.assertThrows(UnauthorizedException.class,
            () -> businessCardController.create(cardRequest),
            "Should throw UnauthorizedException when not logged in");
    }

    @Test
    public void testCreateBusinessCard_InvalidPrivilegeCode_Failure() {
        // Create business account
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create card with invalid privilege code
        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        cardRequest.setPrivilegeCode("INVALID_CODE");

        Assertions.assertThrows(NotFoundException.class,
            () -> businessCardController.create(cardRequest),
            "Should throw ValidationException for invalid privilege code");

    }
}
