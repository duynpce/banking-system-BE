package com.example.banking_system.account.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.service.query.GovernmentAccountQueryService;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.domain.account.controller.GovernmentAccountController;
import com.example.banking_system.domain.account.dto.CreateGovernmentAccountRequest;
import com.example.banking_system.domain.account.dto.UpdateGovernmentAccountRequest;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ConflictDataException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GovernmentAccountIntegrationTest extends IntegrationTest {

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Autowired
    private GovernmentAccountController governmentAccountController;

    @Autowired
    GovernmentAccountQueryService governmentAccountQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateGovernmentAccount_Success(){
        CreateGovernmentAccountRequest request = accountTestCases.getCreateGovernmentAccountRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = governmentAccountController.create(request);

        GovernmentAccount createdAccount = governmentAccountQueryService.findByUsername(request.getUsername());

        assertEquals(createdAccount.getGovernmentDepartment(), request.getGovernmentDepartment(), "Government department should match");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        Assertions.assertNotNull(response.getBody());
        assertEquals("Government account created successfully", response.getBody().getMessage(), "Response body should match");
    }

    @Test
    public void testCreateGovernmentAccount_DuplicatePhoneNumber_Failure(){
        CreateGovernmentAccountRequest request1 = accountTestCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(request1);

        CreateGovernmentAccountRequest request2 = accountTestCases.getCreateGovernmentAccountRequestTestCase();
        request2.setUsername("newUsername");
        request2.setEmail("newEmail@gmail.com");

        Assertions.assertThrows(ConflictDataException.class, () -> governmentAccountController.create(request2), "Phone number already exists");
    }

    @Test
    public void testUpdateGovernmentAccount_Success(){
        CreateGovernmentAccountRequest createRequest = accountTestCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateGovernmentAccountRequest updateRequest = accountTestCases.getUpdateGovernmentAccountRequestTestCase();
        ResponseEntity<ResponseDto<String>> response = governmentAccountController.update(updateRequest);
        GovernmentAccount updatedAccount = governmentAccountQueryService.findByUsername(createRequest.getUsername());

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");

        // Verify updated data
        assertEquals(updateRequest.getEmail(), updatedAccount.getAccount().getEmail(), "Email should be updated");
        assertEquals(updateRequest.getPhoneNumber(), updatedAccount.getAccount().getPhoneNumber(), "Phone number should be updated");
        assertEquals(updateRequest.getAddress(), updatedAccount.getAccount().getAddress(), "Address should be updated");
        assertEquals(updateRequest.getGovernmentDepartment(), updatedAccount.getGovernmentDepartment(), "Government department should be updated");
    }

    @Test
    public void testUpdateGovernmentAccount_AllFieldsNull_Failure(){
        CreateGovernmentAccountRequest createRequest = accountTestCases.getCreateGovernmentAccountRequestTestCase();
        governmentAccountController.create(createRequest);

        when(jwtUtil.getUsername()).thenReturn(createRequest.getUsername());

        UpdateGovernmentAccountRequest updateRequest = new UpdateGovernmentAccountRequest();

        Assertions.assertThrows(ValidationException.class, () -> {
            ResponseEntity<ResponseDto<String>> response = governmentAccountController.update(updateRequest);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response status should be BAD_REQUEST");
        }, "At least one field must be provided for update");
    }
}

