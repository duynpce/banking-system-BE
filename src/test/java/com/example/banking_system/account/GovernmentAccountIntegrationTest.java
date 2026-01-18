package com.example.banking_system.account;

import com.example.banking_system.IntegrationTest;
import com.example.banking_system.controller.account.GovernmentAccountController;
import com.example.banking_system.dto.account.CreateGovernmentAccountRequest;
import com.example.banking_system.dto.account.UpdateGovernmentAccountRequest;
import com.example.banking_system.entity.account.GovernmentAccount;
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

public class GovernmentAccountIntegrationTest extends IntegrationTest {

    private final TestCases testCases = TestCases.getInstance();

    @Autowired
    private GovernmentAccountController governmentAccountController;

    @Autowired
    AccountService accountService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateGovernmentAccount_Success(){
        CreateGovernmentAccountRequest request = testCases.getCreateGovernmentAccountRequestTestCase();

        ResponseEntity<String> response = governmentAccountController.create(request);

        GovernmentAccount createdAccount = (GovernmentAccount) accountService.findByUsername(request.getUsername());

        assertEquals(createdAccount.getGovernmentDepartment(), request.getGovernmentDepartment(), "Government department should match");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertEquals("Government account created successfully", response.getBody(), "Response body should match");
    }

    @Test
    public void testCreateGovernmentAccount_DuplicatePhoneNumber_Failure(){
        CreateGovernmentAccountRequest request1 = testCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(request1);

        CreateGovernmentAccountRequest request2 = testCases.getCreateGovernmentAccountRequestTestCase();
        request2.setUsername("newUsername");
        request2.setEmail("newEmail@gmail.com");

        Assertions.assertThrows(ConflictDataException.class, () -> {
            governmentAccountController.create(request2);
        }, "Phone number already exists");
    }

    @Test
    public void testUpdateGovernmentAccount_Success(){
        CreateGovernmentAccountRequest createRequest = testCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateGovernmentAccountRequest updateRequest = testCases.getUpdateGovernmentAccountRequestTestCase();
        ResponseEntity<String> response = governmentAccountController.update(updateRequest);
        GovernmentAccount updatedAccount = (GovernmentAccount) accountService.findByUsername(createRequest.getUsername());

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        // Verify updated data
        assertEquals(updateRequest.getEmail(), updatedAccount.getEmail(), "Email should be updated");
        assertEquals(updateRequest.getPhoneNumber(), updatedAccount.getPhoneNumber(), "Phone number should be updated");
        assertEquals(updateRequest.getAddress(), updatedAccount.getAddress(), "Address should be updated");
        assertEquals(updateRequest.getGovernmentDepartment(), updatedAccount.getGovernmentDepartment(), "Government department should be updated");
    }

    @Test
    public void testUpdateGovernmentAccount_AllFieldsNull_Failure(){
        CreateGovernmentAccountRequest createRequest = testCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateGovernmentAccountRequest updateRequest = new UpdateGovernmentAccountRequest();

        Assertions.assertThrows(ValidationException.class, () -> {
            ResponseEntity<String> response = governmentAccountController.update(updateRequest);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response status should be BAD_REQUEST");
        }, "At least one field must be provided for update");
    }
}

