package com.example.banking_system.account;


import com.example.banking_system.IntegrationTest;
import com.example.banking_system.controller.account.BusinessAccountController;
import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.dto.account.UpdateBusinessAccountRequest;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.exception.ConflictDataException;
import com.example.banking_system.exception.ValidationException;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BusinessAccountIntegrationTest extends IntegrationTest {

    private final TestCases testCases = TestCases.getInstance();

    @Autowired
    private BusinessAccountController businessAccountController;

    @Autowired
    AccountService accountService;

    @MockitoBean
    private JwtUtil jwtUtil;



    @Test
    public void testCreateBusinessAccount_Success(){
        CreateBusinessAccountRequest request = testCases.getCreateBusinessAccountRequestTestCase();

        ResponseEntity<String> response = businessAccountController.create(request);

        BusinessAccount createdAccount = (BusinessAccount) accountService.findByUsername(request.getUsername());

        assertEquals(createdAccount.getTaxIdNumber(), request.getTaxIdNumber(), "Tax ID number should match");
        assertEquals(createdAccount.getOrganizationName(), request.getOrganizationName(), "Organization name should match" );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals("Business account created successfully", response.getBody(), "Response body should match");

    }

    @Test
    public void testCreateBusinessAccount_DuplicateTaxIdNumber_Failure(){
        CreateBusinessAccountRequest request1 = testCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(request1);

        CreateBusinessAccountRequest request2 = testCases.getCreateBusinessAccountRequestTestCase();
        request2.setUsername("newUsername");
        request2.setEmail("newEmail@gmail.com");
        request2.setPhoneNumber("newPhoneNumber");

        Assertions.assertThrows(ConflictDataException.class, () -> {
            //check exception message and status code
            ResponseEntity<String> response = businessAccountController.create(request2);
            assertEquals(HttpStatus.CONFLICT,response.getStatusCode(), "Response status should be CONFLICT");
            }, "Tax id number already exists");



    }

    @Test
    public void testUpdateBusinessAccount_Success(){

        CreateBusinessAccountRequest createRequest = testCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateBusinessAccountRequest updateRequest = testCases.getUpdateBusinessAccountRequestTestCase();
        ResponseEntity<String> response = businessAccountController.update(updateRequest);
        BusinessAccount updatedAccount = (BusinessAccount) accountService.findByUsername(createRequest.getUsername());

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        // Verify updated data
        assertEquals(updateRequest.getEmail(), updatedAccount.getEmail(), "Email should be updated");
        assertEquals(updateRequest.getPhoneNumber(), updatedAccount.getPhoneNumber(), "Phone number should be updated");
        assertEquals(updateRequest.getAddress(), updatedAccount.getAddress(), "Address should be updated");
        assertEquals(updateRequest.getOrganizationName(), updatedAccount.getOrganizationName(), "Organization name should be updated");
        assertEquals(updateRequest.getTaxIdNumber(), updatedAccount.getTaxIdNumber(), "Tax ID number should be updated");

    }

    @Test
    public void testUpdateBusinessAccount_AllFieldsNull_Failure(){
        CreateBusinessAccountRequest createRequest = testCases.getCreateBusinessAccountRequestTestCase();
        businessAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateBusinessAccountRequest updateRequest = new UpdateBusinessAccountRequest();

        Assertions.assertThrows(ValidationException.class, () -> {
            ResponseEntity<String> response = businessAccountController.update(updateRequest);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response status should be BAD_REQUEST");
        }, "At least one field must be provided for update");
    }

}
