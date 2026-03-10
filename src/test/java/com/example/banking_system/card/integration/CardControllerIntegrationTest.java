package com.example.banking_system.card.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.account.controller.AccountController;
import com.example.banking_system.account.controller.BusinessAccountController;
import com.example.banking_system.account.controller.PersonalAccountController;
import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.card.controller.BusinessCardController;
import com.example.banking_system.card.controller.CardController;
import com.example.banking_system.card.controller.CardPrivilegeController;
import com.example.banking_system.card.controller.PersonalCardController;
import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.service.query.CardQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//not using transaction for commit delete and create operation
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CardControllerIntegrationTest extends IntegrationTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();
    private final CardTestCases cardTestCases = CardTestCases.getInstance();

    @Autowired
    private CardController cardController;

    @Autowired
    private BusinessCardController businessCardController;

    @Autowired
    private PersonalCardController personalCardController;

    @Autowired
    private BusinessAccountController businessAccountController;

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    private AccountController accountController;

    @Autowired
    private CardQueryService cardQueryService;

    @Autowired
    private CardPrivilegeController cardPrivilegeController;

    @Autowired
    private CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    @Autowired
    private CardPrivilegeQueryService cardPrivilegeQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testGetAllFromByJwt_Success() {
        //set up
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create business card
        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(cardRequest);

        // Get all cards
        ResponseEntity<List<? extends GetCardResponse>> response = cardController.getAllFromByJwt();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Card list should not be empty");

        // Clean up
        cardController.delete(response.getBody().getFirst().getId());
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(cardRequest.getPrivilegeCode());
    }

    @Test
    public void testGetAllFromByJwt_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        Assertions.assertThrows(UnauthorizedException.class,
            () -> cardController.getAllFromByJwt(),
            "Should throw UnauthorizedException when not logged in");
    }

    @Test
    public void testGetById_Success() {
        //set up
        CreatePersonalAccountRequest accountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(accountRequest);
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create personal card
        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();
        personalCardController.create(cardRequest);

        // Get all cards to retrieve the card ID
        ResponseEntity<List<? extends GetCardResponse>> allCardsResponse = cardController.getAllFromByJwt();
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsResponse.getBody(), "Response body should not be null");
        long cardId = allCardsResponse.getBody().getFirst().getId();

        // Get card by ID
        ResponseEntity<GetCardResponse> response = cardController.getById(cardId);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(cardId, response.getBody().getId(), "Card ID should match");

        // Clean up
        cardController.delete(cardId);
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(cardRequest.getPrivilegeCode());
    }

    @Test
    public void testGetById_NotOwner_Failure() {
        //set up
        CreateBusinessAccountRequest account1Request = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(account1Request);
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());

        CreateBusinessCardRequest card1Request = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(card1Request);

        ResponseEntity<List<? extends GetCardResponse>> allCardsResponse = cardController.getAllFromByJwt();
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsResponse.getBody(), "Response body should not be null");
        long cardId = allCardsResponse.getBody().getFirst().getId();

        // Create second account
        CreatePersonalAccountRequest account2Request = accountTestCases.getCreatePersonalAccountRequestTestCase();
        // Modify unique required information to avoid conflicts
        account2Request.setUsername("differentUsername");
        account2Request.setIdCardNumber("987654321");
        account2Request.setPhoneNumber("1231231123");
        account2Request.setEmail("differentEmail@gmail.com");
        personalAccountController.create(account2Request);

        // Try to access first user's card with second user's JWT
        when(jwtUtil.getUsername()).thenReturn(account2Request.getUsername());

        Assertions.assertThrows(ForbiddenException.class,
            () -> cardController.getById(cardId),
            "Should throw ForbiddenException when accessing another user's card");

        // Clean up
        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());
        cardController.delete(cardId);
        accountController.delete();

        when(jwtUtil.getUsername()).thenReturn(account2Request.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(card1Request.getPrivilegeCode());
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(card1Request.getPrivilegeCode());
    }

    @Test
    public void testDelete_Success() {
        // set up
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);
        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create business card
        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(cardRequest);

        ResponseEntity<List<? extends GetCardResponse>> allCardsResponse = cardController.getAllFromByJwt();
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsResponse.getBody(), "Response body should not be null");
        long cardId = allCardsResponse.getBody().getFirst().getId();

        // Delete card
        ResponseEntity<GetCardResponse> response = cardController.delete(cardId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Response status should be NO_CONTENT");

        Assertions.assertThrows(NotFoundException.class,
            () -> cardQueryService.findById(cardId),
            "Card should not exist after deletion");

        // Clean up account
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(cardRequest.getPrivilegeCode());
    }

    @Test
    public void testDelete_NotOwner_Failure() {
        //set up
        CreateBusinessAccountRequest account1Request = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(account1Request);

        cardPrivilegeCodeQueryService.save(cardTestCases.getCardPrivilegeCodeTestCase());
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());

        CreateBusinessCardRequest card1Request = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(card1Request);

        ResponseEntity<List<? extends GetCardResponse>> allCardsResponse = cardController.getAllFromByJwt();
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsResponse.getBody(), "Response body should not be null");
        long cardId = allCardsResponse.getBody().getFirst().getId();


        CreatePersonalAccountRequest account2Request = accountTestCases.getCreatePersonalAccountRequestTestCase();
        // Modify unique required information to avoid conflicts
        account2Request.setUsername("differentUsername");
        account2Request.setIdCardNumber("987654321");
        account2Request.setPhoneNumber("1231231123");
        account2Request.setEmail("differentEmail@gmail.com");
        personalAccountController.create(account2Request);

        // Try to delete first user's card with second user's JWT
        when(jwtUtil.getUsername()).thenReturn(account2Request.getUsername());

        Assertions.assertThrows(ForbiddenException.class,
            () -> cardController.delete(cardId),
            "Should throw ForbiddenException when deleting another user's card");

        // Clean up
        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());
        cardController.delete(cardId);
        accountController.delete();

        when(jwtUtil.getUsername()).thenReturn(account2Request.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(card1Request.getPrivilegeCode());
        cardPrivilegeCodeQueryService.deleteByCodeAndIsActive(card1Request.getPrivilegeCode());

    }
}
