package com.example.banking_system.account;

import com.example.banking_system.IntegrationTest;
import com.example.banking_system.controller.account.PersonalAccountController;
import com.example.banking_system.dto.account.CreatePersonalAccountRequest;
import com.example.banking_system.dto.account.UpdatePersonalAccountRequest;
import com.example.banking_system.entity.account.PersonalAccount;
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

public class PersonalAccountIntegrationTest extends IntegrationTest {

    private final TestCases testCases = TestCases.getInstance();

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    AccountService accountService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreatePersonalAccount_Success(){
        CreatePersonalAccountRequest request = testCases.getCreatePersonalAccountRequestTestCase();

        ResponseEntity<String> response = personalAccountController.create(request);

        PersonalAccount createdAccount = (PersonalAccount) accountService.findByUsername(request.getUsername());

        assertEquals(createdAccount.getIdCardNumber(), request.getIdCardNumber(), "ID card number should match");
        assertEquals(createdAccount.getFullName(), request.getFullName(), "Full name should match" );
        assertEquals(createdAccount.getDateOfBirth(), request.getDateOfBirth(), "Date of birth should match" );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals("Personal account created successfully", response.getBody(), "Response body should match");
    }

    @Test
    public void testCreatePersonalAccount_DuplicateIdCardNumber_Failure(){
        CreatePersonalAccountRequest request1 = testCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(request1);

        CreatePersonalAccountRequest request2 = testCases.getCreatePersonalAccountRequestTestCase();
        request2.setUsername("newUsername");
        request2.setEmail("newEmail@gmail.com");
        request2.setPhoneNumber("newPhoneNumber");

        Assertions.assertThrows(ConflictDataException.class, () -> {
            ResponseEntity<String> response = personalAccountController.create(request2);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode(), "Response status should be CONFLICT");
        }, "ID card number already exists");
    }

    @Test
    public void testUpdatePersonalAccount_Success(){
        CreatePersonalAccountRequest createRequest = testCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdatePersonalAccountRequest updateRequest = testCases.getUpdatePersonalAccountRequestTestCase();
        ResponseEntity<String> response = personalAccountController.update(updateRequest);
        PersonalAccount updatedAccount = (PersonalAccount) accountService.findByUsername(createRequest.getUsername());

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        // Verify updated data
        assertEquals(updateRequest.getEmail(), updatedAccount.getEmail(), "Email should be updated");
        assertEquals(updateRequest.getPhoneNumber(), updatedAccount.getPhoneNumber(), "Phone number should be updated");
        assertEquals(updateRequest.getAddress(), updatedAccount.getAddress(), "Address should be updated");
        assertEquals(updateRequest.getFullName(), updatedAccount.getFullName(), "Full name should be updated");
        assertEquals(updateRequest.getDateOfBirth(), updatedAccount.getDateOfBirth(), "Date of birth should be updated");
        assertEquals(updateRequest.getIdCardNumber(), updatedAccount.getIdCardNumber(), "ID card number should be updated");
    }

    @Test
    public void testUpdatePersonalAccount_AllFieldsNull_Failure(){
        CreatePersonalAccountRequest createRequest = testCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdatePersonalAccountRequest updateRequest = new UpdatePersonalAccountRequest();

        Assertions.assertThrows(ValidationException.class, () -> {
            ResponseEntity<String> response = personalAccountController.update(updateRequest);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response status should be BAD_REQUEST");
        }, "At least one field must be provided for update");
    }
}

