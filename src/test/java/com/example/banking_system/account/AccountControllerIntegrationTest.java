package com.example.banking_system.account;

import com.example.banking_system.IntegrationTest;
import com.example.banking_system.controller.account.AccountController;
import com.example.banking_system.controller.account.BusinessAccountController;
import com.example.banking_system.controller.account.GovernmentAccountController;
import com.example.banking_system.controller.account.PersonalAccountController;
import com.example.banking_system.dto.account.*;
import com.example.banking_system.exception.NotFoundException;
import com.example.banking_system.exception.UnauthorizedException;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

public class AccountControllerIntegrationTest extends IntegrationTest {

    private final TestCases testCases = TestCases.getInstance();

    @Autowired
    private AccountController accountController;

    @Autowired
    private BusinessAccountController businessAccountController;

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    private GovernmentAccountController governmentAccountController;

    @Autowired
    private AccountService accountService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void should_fail(){
        Assertions.fail();
    }

    @Test
    public void testGet_BusinessAccount_Success() {
        CreateBusinessAccountRequest createRequest = testCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountRequest> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertInstanceOf(GetBusinessAccountRequest.class, response.getBody(), "Response body should be GetBusinessAccountRequest");

        GetBusinessAccountRequest businessAccount = (GetBusinessAccountRequest) response.getBody();
        assertEquals(createRequest.getUsername(), businessAccount.getUsername(), "Username should match");
        assertEquals(createRequest.getOrganizationName(), businessAccount.getOrganizationName(), "Organization name should match");
    }

    @Test
    public void testGet_PersonalAccount_Success() {
        CreatePersonalAccountRequest createRequest = testCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountRequest> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertInstanceOf(GetPersonalAccountRequest.class, response.getBody(), "Response body should be GetPersonalAccountRequest");

        GetPersonalAccountRequest personalAccount = (GetPersonalAccountRequest) response.getBody();
        assertEquals(createRequest.getUsername(), personalAccount.getUsername(), "Username should match");
        assertEquals(createRequest.getFullName(), personalAccount.getFullName(), "Full name should match");
    }

    @Test
    public void testGet_GovernmentAccount_Success() {
        CreateGovernmentAccountRequest createRequest = testCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<GetAccountRequest> response = accountController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertInstanceOf(GetGovernmentAccountRequest.class, response.getBody(), "Response body should be GetGovernmentAccountRequest");

        GetGovernmentAccountRequest governmentAccount = (GetGovernmentAccountRequest) response.getBody();
        assertEquals(createRequest.getUsername(), governmentAccount.getUsername(), "Username should match");
        assertEquals(createRequest.getGovernmentDepartment(), governmentAccount.getGovernmentDepartment(), "Government department should match");
    }

    @Test
    public void testGet_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            accountController.get();
        }, "has not logged in");
    }

    @Test
    public void testDelete_Success() {
        CreateBusinessAccountRequest createRequest = testCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        ResponseEntity<String> response = accountController.delete();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals("Account deleted successfully", response.getBody(), "Response message should match");

        Assertions.assertThrows(NotFoundException.class, () -> {
            accountService.findByUsername(createRequest.getUsername());
        }, "User not found with username: " + createRequest.getUsername());
    }

    @Test
    public void testDelete_NotLoggedIn_Failure() {
        when(jwtUtil.getUsername()).thenThrow(new UnauthorizedException("has not logged in"));

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            accountController.delete();
        }, "has not logged in");
    }
}

