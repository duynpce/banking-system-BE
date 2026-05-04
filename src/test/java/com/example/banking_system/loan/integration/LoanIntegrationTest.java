package com.example.banking_system.loan.integration;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.controller.PersonalAccountController;
import com.example.banking_system.domain.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.controller.LoanController;
import com.example.banking_system.domain.loan.controller.LoanPolicyController;
import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.dto.LoanFilter;
import com.example.banking_system.loan.LoanTestCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanIntegrationTest extends IntegrationTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Autowired
    private LoanController loanController;

    @Autowired
    private LoanPolicyController loanPolicyController;

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    private AccountQueryService accountQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateLoanSuccess() {
        setupLoanScenario();
        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        request.setPolicyId(loanPolicyResponse.getId());
        Account account = accountQueryService.findById(jwtUtil.getJwtClaims().getClaim("account_id"));
        BigDecimal initialBalance = account.getBalance();

        ResponseEntity<ResponseDto<String>> response = loanController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertEquals(account.getBalance(), initialBalance.add(request.getAmount()), "Account balance should be increased by loan amount");

    }

    @Test
    public void testCreateLoanFailureValidationError() {
        setupLoanScenario();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        request.setPolicyId(loanPolicyResponse.getId());
        request.setAmount(loanPolicyResponse.getMaxAmount().add(BigDecimal.ONE));

        Assertions.assertThrows(ValidationException.class,
                () -> loanController.create(request),
                "Should throw ValidationException when loan request is invalid");
    }

    @Test
    public void testGetLoanByIdSuccess() {
        setupLoanScenario();

        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        request.setPolicyId(loanPolicyResponse.getId());
        loanController.create(request);

        LoanFilter filter = loanTestCases.getLoanFilterTestCase();
        GetLoanResponse getLoanResponse = Objects.requireNonNull(loanController.getByFilter(filter).getBody()).getData().getFirst();

        ResponseEntity<ResponseDto<GetLoanResponse>> responseEntity = loanController.getById(getLoanResponse.getId());

        assertNotNull(responseEntity.getBody());
        GetLoanResponse response = responseEntity.getBody().getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status should be OK");
        assertNotNull(responseEntity.getBody(), "Response body should not be null");
        assertTrue(responseEntity.getBody().isSuccess(), "Response success flag should be true");
        assertNotNull(response, "Response data should not be null");
        assertEquals(request.getAmount(), response.getBaseAmount(), "Loan amount should match the request");
        assertEquals(request.getType(), response.getType(), "Loan type should match the request");
    }

    @Test
    public void testGetLoanByIdNotFoundFailure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> loanController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    public void testGetLoanByFilterSuccess() {
        setupLoanScenario();

        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        request.setPolicyId(loanPolicyResponse.getId());

        loanController.create(request);

        LoanFilter loanFilter = loanTestCases.getLoanFilterTestCase();
        ResponseEntity<ResponseDto<List<GetLoanResponse>>> responseEntity = loanController.getByFilter(loanFilter);
        assertNotNull(responseEntity.getBody());
        List<GetLoanResponse> response = responseEntity.getBody().getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status should be OK");
        assertNotNull(responseEntity.getBody(), "Response body should not be null");
        assertTrue(responseEntity.getBody().isSuccess(), "Response success flag should be true");
        assertNotNull(response, "Response data should not be null");
        assertFalse(response.isEmpty(), "Response data should not be empty");
        assertEquals(request.getAmount(), response.getFirst().getBaseAmount(), "Base amount should match the request amount");
        assertEquals(request.getType(), response.getFirst().getType(), "Loan type should match the request");
    }

    @Test
    public void testGetLoanByFilterNoMatchingStatus() {
        setupLoanScenario();

        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        request.setPolicyId(loanPolicyResponse.getId());
        loanController.create(request);

        LoanFilter loanFilter = loanTestCases.getLoanFilterTestCase();
        loanFilter.setStatus(LoanStatus.DONE_PAYMENT);
        ResponseEntity<ResponseDto<List<GetLoanResponse>>> responseEntity = loanController.getByFilter(loanFilter);

        assertNotNull(responseEntity.getBody());
        List<GetLoanResponse> response = responseEntity.getBody().getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status should be OK");
        assertTrue(responseEntity.getBody().isSuccess(), "Response success flag should be true");
        assertTrue(response.isEmpty(), "Response data should be empty when no loans match the filter");
    }

    @Test
    public void testGetLoanByReportsSuccess() {
        setupLoanScenario();

        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase();
        GetLoanPolicyResponse loanPolicyResponse = Objects.requireNonNull(loanPolicyController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        request.setPolicyId(loanPolicyResponse.getId());
        loanController.create(request);

        ResponseEntity<ResponseDto<GetLoanReportResponse>> responseEntity = loanController.getByReports(LoanStatus.CURRENT_PAYMENT);

        assertNotNull(responseEntity.getBody());
        GetLoanReportResponse response = responseEntity.getBody().getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status should be OK");
        assertTrue(responseEntity.getBody().isSuccess(), "Response success flag should be true");
        assertNotNull(response, "Response data should not be null");
        assertEquals(LoanStatus.CURRENT_PAYMENT, response.getLoanStatus(), "Report status should match requested status");
        assertNotNull(response.getMonthlyInstallment(), "Monthly installment should not be null");
    }

    @Test
    public void testGetLoanByReportsNoLoans() {
        setupLoanScenario();

        ResponseEntity<ResponseDto<GetLoanReportResponse>> responseEntity = loanController.getByReports(LoanStatus.DONE_PAYMENT);

        assertNotNull(responseEntity.getBody());
        GetLoanReportResponse response = responseEntity.getBody().getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Response status should be OK");
        assertTrue(responseEntity.getBody().isSuccess(), "Response success flag should be true");
        assertNotNull(response, "Response data should not be null");
        assertEquals(LoanStatus.DONE_PAYMENT, response.getLoanStatus(), "Report status should match requested status");
        assertEquals(0, response.getTotalAmount().compareTo(BigDecimal.ZERO), "Total amount should be 0 when no loans");
        assertEquals(0, response.getLeftAmount().compareTo(BigDecimal.ZERO), "Left amount should be 0 when no loans");
        assertEquals(0, response.getMonthlyInstallment().compareTo(BigDecimal.ZERO), "Monthly installment should be 0 when no loans");
    }


    private void setupLoanScenario() {
        CreatePersonalAccountRequest createAccountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createAccountRequest);
        Account account = accountQueryService.findByUsername(createAccountRequest.getUsername());
        account.setBalance(new BigDecimal("500.00"));
        accountQueryService.save(account);

        CreateLoanPolicyRequest policyRequest = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(policyRequest);

        Jwt jwt = new Jwt(
                "test-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("account_id", account.getId())
        );
        when(jwtUtil.getJwtClaims()).thenReturn(jwt);

    }

}
