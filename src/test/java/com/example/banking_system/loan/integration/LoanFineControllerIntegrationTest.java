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
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.controller.LoanController;
import com.example.banking_system.domain.loan.controller.LoanFineController;
import com.example.banking_system.domain.loan.controller.LoanFinePolicyController;
import com.example.banking_system.domain.loan.controller.LoanPolicyController;
import com.example.banking_system.domain.loan.dto.*;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
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
import static org.mockito.Mockito.when;

public class LoanFineControllerIntegrationTest extends IntegrationTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Autowired
    private LoanFineController loanFineController;

    @Autowired
    private LoanPolicyController loanPolicyController;

    @Autowired
    private LoanFinePolicyController loanFinePolicyController;

    @Autowired
    private PersonalAccountController personalAccountController;

    @Autowired
    private LoanController loanController;

    @Autowired
    private AccountQueryService accountQueryService;

    @Autowired
    private LoanPolicyQueryService  loanPolicyQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateLoanFineSuccess() {
        setupLoan();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        GetLoanResponse loanResponse = Objects.requireNonNull(loanController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        GetLoanFinePolicyResponse loanFinePolicyResponse = Objects.requireNonNull(loanFinePolicyController.getByLoanFineType(LoanFineType.OVERDUE_PAYMENT).getBody()).getData().getFirst();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loanResponse.getId(), accountId, loanFinePolicyResponse.getId());


        ResponseEntity<ResponseDto<String>> response = loanFineController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        GetLoanFineResponse created = Objects.requireNonNull(loanFineController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        assertEquals(request.getAmount(), created.getAmount(), "Loan fine amount should match");
        assertEquals(request.getType(), created.getType(), "Loan fine type should match");
    }

    @Test
    public void testCreateLoanFineFailureValidationError() {
        setupLoan();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        GetLoanResponse loanResponse = Objects.requireNonNull(loanController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        GetLoanFinePolicyResponse loanFinePolicyResponse = Objects.requireNonNull(loanFinePolicyController.getByLoanFineType(LoanFineType.OVERDUE_PAYMENT).getBody()).getData().getFirst();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loanResponse.getId(), accountId, loanFinePolicyResponse.getId());
        request.setAmount(BigDecimal.ZERO);

        Assertions.assertThrows(ValidationException.class,
                () -> loanFineController.create(request),
                "Should throw ValidationException when amount is invalid");
    }

    @Test
    public void testUpdateLoanFineSuccess() {
        setupLoan();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        GetLoanResponse loanResponse = Objects.requireNonNull(loanController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        GetLoanFinePolicyResponse loanFinePolicyResponse = Objects.requireNonNull(loanFinePolicyController.getByLoanFineType(LoanFineType.OVERDUE_PAYMENT).getBody()).getData().getFirst();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loanResponse.getId(), accountId, loanFinePolicyResponse.getId());
        loanFineController.create(request);
        GetLoanFineResponse created = Objects.requireNonNull(loanFineController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();

        UpdateLoanFineRequest updateRequest = loanTestCases.getUpdateLoanFineRequestTestCase(created.getId());
        ResponseEntity<ResponseDto<String>> response = loanFineController.update(updateRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
    }

    @Test
    public void testUpdateLoanFineNotFoundFailure() {
        UpdateLoanFineRequest request = loanTestCases.getUpdateLoanFineRequestTestCase(0L);

        Assertions.assertThrows(NotFoundException.class,
                () -> loanFineController.update(request),
                "Should throw NotFoundException when loan fine not found");
    }

    @Test
    public void testGetLoanFineByIdSuccess() {
        setupLoan();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        GetLoanResponse loanResponse = Objects.requireNonNull(loanController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        GetLoanFinePolicyResponse loanFinePolicyResponse = Objects.requireNonNull(loanFinePolicyController.getByLoanFineType(LoanFineType.OVERDUE_PAYMENT).getBody()).getData().getFirst();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loanResponse.getId(), accountId, loanFinePolicyResponse.getId());
        loanFineController.create(request);
        GetLoanFineResponse created = Objects.requireNonNull(loanFineController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();

        ResponseEntity<ResponseDto<GetLoanFineResponse>> response = loanFineController.getById(created.getId());
        ResponseDto<GetLoanFineResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(created.getId(), responseDto.getData().getId(), "Loan fine id should match");
    }

    @Test
    public void testGetLoanFineByIdNotFoundFailure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> loanFineController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    public void testGetLoanFineByPageSuccess() {
        setupLoan();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        GetLoanResponse loanResponse = Objects.requireNonNull(loanController.getByPage(new PaginationDto(0, 5)).getBody()).getData().getFirst();
        GetLoanFinePolicyResponse loanFinePolicyResponse = Objects.requireNonNull(loanFinePolicyController.getByLoanFineType(LoanFineType.OVERDUE_PAYMENT).getBody()).getData().getFirst();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loanResponse.getId(), accountId, loanFinePolicyResponse.getId());
        loanFineController.create(request);

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);

        ResponseEntity<ResponseDto<List<GetLoanFineResponse>>> response = loanFineController.getByPage(paginationDto);
        ResponseDto<List<GetLoanFineResponse>> responseDto = response.getBody();
        List<GetLoanFineResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");
    }

    private void setupLoan() {
        CreatePersonalAccountRequest createAccountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
        personalAccountController.create(createAccountRequest);
        Account account = accountQueryService.findByUsername(createAccountRequest.getUsername());
        account.setBalance(new BigDecimal("500.00"));
        accountQueryService.save(account);

        Jwt jwt = new Jwt(
                "test-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of(
                        "account_id", account.getId(),
                        "account_number", account.getNumber()
                )
        );
        when(jwtUtil.getJwtClaims()).thenReturn(jwt);


        CreateLoanPolicyRequest createLoanPolicyRequest = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(createLoanPolicyRequest);

        CreateLoanFinePolicyRequest createLoanFinePolicyRequest = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        loanFinePolicyController.create(createLoanFinePolicyRequest);

        LoanPolicy loanPolicy = loanPolicyQueryService.findByLoanTypeAndIsActive(createLoanPolicyRequest.getLoanType()).getFirst();
        CreateLoanRequest createLoanRequest =  loanTestCases.getCreateLoanRequestTestCase();
        createLoanRequest.setPolicyId(loanPolicy.getId());
        loanController.create(createLoanRequest);

    }

}
