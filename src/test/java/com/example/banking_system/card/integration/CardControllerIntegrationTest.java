package com.example.banking_system.card.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.controller.AccountController;
import com.example.banking_system.domain.account.controller.BusinessAccountController;
import com.example.banking_system.domain.account.controller.PersonalAccountController;
import com.example.banking_system.domain.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.domain.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.card.CardTestCases;
import com.example.banking_system.domain.card.controller.BusinessCardController;
import com.example.banking_system.domain.card.controller.CardController;
import com.example.banking_system.domain.card.controller.CardPrivilegeController;
import com.example.banking_system.domain.card.controller.PersonalCardController;
import com.example.banking_system.domain.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.domain.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.domain.card.dto.GetCardResponse;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.domain.card.service.query.CardQueryService;
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
    private CardPrivilegeQueryService cardPrivilegeQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testGetAllFromByJwt_Success() {
        //set up
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create business card
        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(cardRequest);

        // Get all cards
        ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> response = cardController.getAllFromByJwt();
        ResponseDto<List<? extends GetCardResponse>> responseDto = response.getBody();
        List<? extends GetCardResponse> cardList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(cardList, "Card list should not be null");
        assertFalse(cardList.isEmpty(), "Card list should not be empty");

        // Clean up
        cardController.delete(cardList.getFirst().getId());
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
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
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create personal card
        CreatePersonalCardRequest cardRequest = cardTestCases.getCreatePersonalCardRequestTestCase();
        personalCardController.create(cardRequest);

        // Get all cards to retrieve the card ID
        ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> allCardsResponse = cardController.getAllFromByJwt();
        ResponseDto<List<? extends GetCardResponse>> allCardsDto = allCardsResponse.getBody();
        List<? extends GetCardResponse> allCards = allCardsDto != null ? allCardsDto.getData() : null;
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsDto, "Response body should not be null");
        assertTrue(allCardsDto.isSuccess(), "Response success flag should be true");
        assertNotNull(allCards, "Card list should not be null");
        long cardId = allCards.getFirst().getId();

        // Get card by ID
        ResponseEntity<ResponseDto<GetCardResponse>> response = cardController.getById(cardId);
        ResponseDto<GetCardResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Card DTO should not be null");
        assertEquals(cardId, responseDto.getData().getId(), "Card ID should match");

        // Clean up
        cardController.delete(cardId);
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
    }

    @Test
    public void testGetById_NotOwner_Failure() {
        //set up
        CreateBusinessAccountRequest account1Request = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(account1Request);
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());

        CreateBusinessCardRequest card1Request = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(card1Request);

        ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> allCardsResponse = cardController.getAllFromByJwt();
        ResponseDto<List<? extends GetCardResponse>> allCardsDto = allCardsResponse.getBody();
        List<? extends GetCardResponse> allCards = allCardsDto != null ? allCardsDto.getData() : null;
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsDto, "Response body should not be null");
        assertTrue(allCardsDto.isSuccess(), "Response success flag should be true");
        assertNotNull(allCards, "Card list should not be null");
        long cardId = allCards.getFirst().getId();

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
    }

    @Test
    public void testDelete_Success() {
        // set up
        CreateBusinessAccountRequest accountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(accountRequest);
        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());

        // Create business card
        CreateBusinessCardRequest cardRequest = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(cardRequest);

        ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> allCardsResponse = cardController.getAllFromByJwt();
        ResponseDto<List<? extends GetCardResponse>> allCardsDto = allCardsResponse.getBody();
        List<? extends GetCardResponse> allCards = allCardsDto != null ? allCardsDto.getData() : null;
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsDto, "Response body should not be null");
        assertTrue(allCardsDto.isSuccess(), "Response success flag should be true");
        assertNotNull(allCards, "Card list should not be null");
        long cardId = allCards.getFirst().getId();

        // Delete card
        ResponseEntity<?> response = cardController.delete(cardId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Response status should be OK");

        Assertions.assertThrows(NotFoundException.class,
            () -> cardQueryService.findById(cardId),
            "Card should not exist after deletion");

        // Clean up account
        when(jwtUtil.getUsername()).thenReturn(accountRequest.getUsername());
        accountController.delete();

        cardPrivilegeQueryService.deleteByPrivilegeCode(cardRequest.getPrivilegeCode());
    }

    @Test
    public void testDelete_NotOwner_Failure() {
        //set up
        CreateBusinessAccountRequest account1Request = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(account1Request);

        cardPrivilegeController.create(cardTestCases.getCreateCardPrivilegeRequestTestCase());

        when(jwtUtil.getUsername()).thenReturn(account1Request.getUsername());

        CreateBusinessCardRequest card1Request = cardTestCases.getCreateBusinessCardRequestTestCase();
        businessCardController.create(card1Request);

        ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> allCardsResponse = cardController.getAllFromByJwt();
        ResponseDto<List<? extends GetCardResponse>> allCardsDto = allCardsResponse.getBody();
        List<? extends GetCardResponse> allCards = allCardsDto != null ? allCardsDto.getData() : null;
        assertNotNull(allCardsResponse, "Response should not be null");
        assertNotNull(allCardsDto, "Response body should not be null");
        assertTrue(allCardsDto.isSuccess(), "Response success flag should be true");
        assertNotNull(allCards, "Card list should not be null");
        long cardId = allCards.getFirst().getId();


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

    }
}
