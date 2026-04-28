package com.example.banking_system.loan.integration;

import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.controller.LoanFinePolicyController;
import com.example.banking_system.domain.loan.dto.CreateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFinePolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.repository.LoanFinePolicyRepository;
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
public class LoanFinePolicyControllerIntegrationTest extends IntegrationTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();

    @Autowired
    private LoanFinePolicyController loanFinePolicyController;

    @Autowired
    private LoanFinePolicyRepository loanFinePolicyRepository;

    @Test
    public void testCreateLoanFinePolicySuccess() {
        CreateLoanFinePolicyRequest request = loanTestCases.getCreateLoanFinePolicyRequestTestCase();

        ResponseEntity<ResponseDto<String>> response = loanFinePolicyController.create(request);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        LoanFinePolicy result = findLoanFinePolicy(request.getLoanFineType(), request.getEffectiveFrom(), request.getEffectiveTo());
        assertEquals(request.getLoanFineType(), result.getLoanFineType(), "Loan fine policy type should match");
        assertEquals(request.getAmount(), result.getAmount(), "Loan fine policy amount should match");

        loanFinePolicyRepository.delete(result);
    }

    @Test
    public void testCreateLoanFinePolicyOverlapFailure() {
        CreateLoanFinePolicyRequest request = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        loanFinePolicyController.create(request);

        Assertions.assertThrows(ValidationException.class,
                () -> loanFinePolicyController.create(request),
                "Should throw ValidationException for overlapping loan fine policy");

        LoanFinePolicy created = findLoanFinePolicy(request.getLoanFineType(), request.getEffectiveFrom(), request.getEffectiveTo());
        loanFinePolicyRepository.delete(created);
    }

    @Test
    public void testUpdateLoanFinePolicySuccess() {
        CreateLoanFinePolicyRequest createRequest = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        loanFinePolicyController.create(createRequest);
        LoanFinePolicy created = findLoanFinePolicy(createRequest.getLoanFineType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        UpdateLoanFinePolicyRequest updateRequest = loanTestCases.getUpdateLoanFinePolicyRequestTestCase();
        updateRequest.setId(created.getId());

        ResponseEntity<ResponseDto<String>> response = loanFinePolicyController.update(updateRequest);
        ResponseDto<String> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");

        loanFinePolicyRepository.deleteById(created.getId());
    }

    @Test
    public void testUpdateLoanFinePolicyNotFoundFailure() {
        UpdateLoanFinePolicyRequest request = loanTestCases.getUpdateLoanFinePolicyRequestTestCase();
        request.setId(0L);

        Assertions.assertThrows(NotFoundException.class,
                () -> loanFinePolicyController.update(request),
                "Should throw NotFoundException when loan fine policy not found");
    }

    @Test
    public void testGetLoanFinePolicyByIdSuccess() {
        CreateLoanFinePolicyRequest createRequest = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        loanFinePolicyController.create(createRequest);
        LoanFinePolicy created = findLoanFinePolicy(createRequest.getLoanFineType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        ResponseEntity<ResponseDto<GetLoanFinePolicyResponse>> response = loanFinePolicyController.getById(created.getId());
        ResponseDto<GetLoanFinePolicyResponse> responseDto = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseDto.getData(), "Response data should not be null");
        assertEquals(created.getId(), responseDto.getData().getId(), "Loan fine policy id should match");

        loanFinePolicyRepository.delete(created);
    }

    @Test
    public void testGetLoanFinePolicyByIdNotFoundFailure() {
        Assertions.assertThrows(NotFoundException.class,
                () -> loanFinePolicyController.getById(Long.MAX_VALUE),
                "Should throw NotFoundException when id does not exist");
    }

    @Test
    public void testGetLoanFinePolicyByPageSuccess() {
        CreateLoanFinePolicyRequest createRequest = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        loanFinePolicyController.create(createRequest);
        LoanFinePolicy created = findLoanFinePolicy(createRequest.getLoanFineType(), createRequest.getEffectiveFrom(), createRequest.getEffectiveTo());

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);

        ResponseEntity<ResponseDto<List<GetLoanFinePolicyResponse>>> response = loanFinePolicyController.getByPage(paginationDto);
        ResponseDto<List<GetLoanFinePolicyResponse>> responseDto = response.getBody();
        List<GetLoanFinePolicyResponse> responseList = responseDto != null ? responseDto.getData() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(responseDto, "Response body should not be null");
        assertTrue(responseDto.isSuccess(), "Response success flag should be true");
        assertNotNull(responseList, "Response data should not be null");
        assertFalse(responseList.isEmpty(), "Response data should not be empty");

        loanFinePolicyRepository.delete(created);
    }

    @Test
    public void testGetLoanFinePolicyByPageInvalidPageFailure() {
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(-1);
        paginationDto.setLimit(10);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> loanFinePolicyController.getByPage(paginationDto),
                "Should throw IllegalArgumentException when page is negative");
    }

    private LoanFinePolicy findLoanFinePolicy(LoanFineType type, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return loanFinePolicyRepository.findAll().stream()
                .filter(policy -> policy.getLoanFineType() == type)
                .filter(policy -> policy.getEffectiveFrom().equals(effectiveFrom))
                .filter(policy -> policy.getEffectiveTo().equals(effectiveTo))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Loan fine policy not found for cleanup"));
    }
}

