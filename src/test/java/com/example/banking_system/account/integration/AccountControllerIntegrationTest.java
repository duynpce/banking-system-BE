package com.example.banking_system.account.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.account.controller.AccountController;
import com.example.banking_system.account.controller.BusinessAccountController;
import com.example.banking_system.account.controller.GovernmentAccountController;
import com.example.banking_system.account.controller.PersonalAccountController;
import com.example.banking_system.account.dto.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//not using transaction for commit delete and create operation
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AccountControllerIntegrationTest extends IntegrationTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Autowired
    private AccountController accountController;

    @Autowired
    private BusinessAccountController businessAccountController;

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    private GovernmentAccountController governmentAccountController;

    @Autowired
    private AccountQueryService accountQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testGet_BusinessAccount_Success() {
        CreateBusinessAccountRequest createRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);


        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountResponse> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        GetBusinessAccountResponse businessAccount = (GetBusinessAccountResponse) response.getBody();
        assertNotNull(businessAccount);
        assertEquals(createRequest.getOrganizationName(), businessAccount.getOrganizationName(), "Organization name should match");
        assertEquals(createRequest.getEmail(), businessAccount.getEmail(), "Username should match");

        // Clean up
        accountController.delete();
    }

    @Test
    public void testGet_PersonalAccount_Success() {
        CreatePersonalAccountRequest createRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountResponse> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        GetPersonalAccountResponse personalAccount = (GetPersonalAccountResponse) response.getBody();
        assertNotNull(personalAccount);
        assertEquals(createRequest.getFullName(), personalAccount.getFullName(), "Full name should match");

        // Clean up
        accountController.delete();
    }

    @Test
    public void testGet_GovernmentAccount_Success() {
        CreateGovernmentAccountRequest createRequest = accountTestCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountResponse> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        GetGovernmentAccountResponse governmentAccount = (GetGovernmentAccountResponse) response.getBody();
        assertNotNull(governmentAccount);
        assertEquals(createRequest.getGovernmentDepartment(), governmentAccount.getGovernmentDepartment(), "Government department should match");

        // Clean up
        accountController.delete();
    }

    @Test
    public void testGet_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        Assertions.assertThrows(UnauthorizedException.class, () -> accountController.get(), "has not logged in");
    }

    @Test
    public void testDelete_Success() {
        CreateBusinessAccountRequest createRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);
        final String username = createRequest.getUsername();

        when(jwtUtil.getUsername()).thenReturn(username);

        ResponseEntity<String> response = accountController.delete();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals("Account deleted successfully", response.getBody(), "Response message should match");

        Assertions.assertThrows(NotFoundException.class, () -> accountQueryService.findByUsername(username), "User not found with username: " + username);
    }

    @Test
    public void testDelete_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        Assertions.assertThrows(UnauthorizedException.class, () -> accountController.delete(), "has not logged in");
    }
}

