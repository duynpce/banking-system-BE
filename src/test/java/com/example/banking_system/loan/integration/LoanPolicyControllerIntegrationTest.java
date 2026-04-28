package com.example.banking_system.loan.integration;

import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.controller.LoanPolicyController;
import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanPolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.repository.LoanPolicyRepository;
import com.example.banking_system.loan.LoanTestCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LoanPolicyControllerIntegrationTest extends IntegrationTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();

    @Autowired
    private LoanPolicyController loanPolicyController;

    @Autowired
    private LoanPolicyRepository loanPolicyRepository;

    @Test
    public void testCreateLoanPolicySuccess() {
        CreateLoanPolicyRequest request = loanTestCases.getCreateLoanPolicyRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = loanPolicyController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        LoanPolicy result = findLoanPolicy(request.getLoanType(), request.getEffectiveFrom(), request.getEffectiveTo());
        assertEquals(request.getLoanType(), result.getLoanType(), "Loan policy type should match");
        assertEquals(request.getDurationMonths(), result.getDurationMonths(), "Loan policy duration should match");

        loanPolicyRepository.delete(result);
    }

    @Test
    public void testCreateLoanPolicyOverlapFailure() {
        CreateLoanPolicyRequest request = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(request);

        Assertions.assertThrows(ValidationException.class,
                () -> loanPolicyController.create(request),
                "Should throw ValidationException for overlapping loan policy");

        LoanPolicy created = findLoanPolicy(request.getLoanType(), request.getEffectiveFrom(), request.getEffectiveTo());
        loanPolicyRepository.delete(created);
    }

    @Test
    public void testUpdateLoanPolicySuccess() {
        CreateLoanPolicyRequest createRequest = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(createRequest);
        LoanPolicy created = findLoanPolicy(createRequest.getLoanType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        UpdateLoanPolicyRequest updateRequest = loanTestCases.getUpdateLoanPolicyRequestTestCase();
        updateRequest.setId(created.getId());

        ResponseEntity<ResponseDto<String>> response = loanPolicyController.update(updateRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        loanPolicyRepository.deleteById(created.getId());
    }

    @Test
    public void testUpdateLoanPolicyNotFoundFailure() {
        UpdateLoanPolicyRequest request = loanTestCases.getUpdateLoanPolicyRequestTestCase();
        request.setId(0L);

        Assertions.assertThrows(NotFoundException.class,
                () -> loanPolicyController.update(request),
                "Should throw NotFoundException when loan policy not found");
    }

    @Test
    public void testGetLoanPolicyByIdSuccess() {
        CreateLoanPolicyRequest createRequest = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(createRequest);
        LoanPolicy created = findLoanPolicy(createRequest.getLoanType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        ResponseEntity<ResponseDto<GetLoanPolicyResponse>> response = loanPolicyController.getById(created.getId());
        ResponseDto<GetLoanPolicyResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(created.getId(), responseDto.getData().getId(), "Loan policy id should match");

        loanPolicyRepository.delete(created);
    }

    @Test
    public void testGetLoanPolicyByIdNotFoundFailure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> loanPolicyController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    public void testGetLoanPolicyByPageSuccess() {
        CreateLoanPolicyRequest createRequest = loanTestCases.getCreateLoanPolicyRequestTestCase();
        loanPolicyController.create(createRequest);
        LoanPolicy created = findLoanPolicy(createRequest.getLoanType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);

        ResponseEntity<ResponseDto<List<GetLoanPolicyResponse>>> response = loanPolicyController.getByPage(paginationDto);
        ResponseDto<List<GetLoanPolicyResponse>> responseDto = response.getBody();
        List<GetLoanPolicyResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        loanPolicyRepository.delete(created);
    }

    @Test
    public void testGetLoanPolicyByPageInvalidPageFailure() {
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(-1);
        paginationDto.setLimit(10);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> loanPolicyController.getByPage(paginationDto),
                "Should throw IllegalArgumentException when page is negative");
    }

    private LoanPolicy findLoanPolicy(LoanType loanType, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return loanPolicyRepository.findAll().stream()
                .filter(policy -> policy.getLoanType() == loanType)
                .filter(policy -> policy.getEffectiveFrom().equals(effectiveFrom))
                .filter(policy -> policy.getEffectiveTo().equals(effectiveTo))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Loan policy not found for cleanup"));
    }
}

