package com.example.banking_system.loan.unit;

import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanPolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.mapper.LoanPolicyMapper;
import com.example.banking_system.domain.loan.service.domain.LoanPolicyService;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
import com.example.banking_system.domain.loan.validator.LoanPolicyValidator;
import com.example.banking_system.loan.LoanTestCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.Mockito.*;

public class LoanPolicyServiceUnitTest extends UnitTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();

    @Mock
    private LoanPolicyQueryService loanPolicyQueryService;

    @Mock
    private LoanPolicyMapper loanPolicyMapper;

    @Mock
    private LoanPolicyValidator loanPolicyValidator;

    @InjectMocks
    private LoanPolicyService loanPolicyService;

    @Test
    public void createLoanPolicySuccess() {
        CreateLoanPolicyRequest request = loanTestCases.getCreateLoanPolicyRequestTestCase();
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();
        LoanPolicy savedLoanPolicy = loanTestCases.getLoanPolicyTestCase();

        when(loanPolicyMapper.toEntity(request)).thenReturn(loanPolicy);
        doNothing().when(loanPolicyValidator).validateCreate(loanPolicy);
        when(loanPolicyQueryService.save(loanPolicy)).thenReturn(savedLoanPolicy);

        LoanPolicy result = loanPolicyService.create(request);

        Assertions.assertEquals(savedLoanPolicy, result);
        verify(loanPolicyMapper).toEntity(request);
        verify(loanPolicyValidator).validateCreate(loanPolicy);
        verify(loanPolicyQueryService).save(loanPolicy);
    }

    @Test
    public void createLoanPolicyFailureValidationError() {
        CreateLoanPolicyRequest request = loanTestCases.getCreateLoanPolicyRequestTestCase();
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();

        when(loanPolicyMapper.toEntity(request)).thenReturn(loanPolicy);
        doThrow(new ValidationException("effective to date must be after effective from date"))
                .when(loanPolicyValidator).validateCreate(loanPolicy);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanPolicyService.create(request)
        );

        Assertions.assertEquals("effective to date must be after effective from date", exception.getMessage());
        verify(loanPolicyMapper).toEntity(request);
        verify(loanPolicyValidator).validateCreate(loanPolicy);
        verify(loanPolicyQueryService, never()).save(any());
    }

    @Test
    public void updateLoanPolicySuccess() {
        UpdateLoanPolicyRequest request = loanTestCases.getUpdateLoanPolicyRequestTestCase();
        LoanPolicy existingLoanPolicy = loanTestCases.getLoanPolicyTestCase();
        LoanPolicy savedLoanPolicy = loanTestCases.getLoanPolicyTestCase();

        when(loanPolicyQueryService.findById(request.getId())).thenReturn(existingLoanPolicy);
        doNothing().when(loanPolicyValidator).validateUpdate(request, existingLoanPolicy);
        when(loanPolicyQueryService.save(existingLoanPolicy)).thenReturn(savedLoanPolicy);

        LoanPolicy result = loanPolicyService.update(request);

        Assertions.assertEquals(savedLoanPolicy, result);
        verify(loanPolicyQueryService).findById(request.getId());
        verify(loanPolicyValidator).validateUpdate(request, existingLoanPolicy);
        verify(loanPolicyQueryService).save(existingLoanPolicy);
    }

    @Test
    public void updateLoanPolicyFailureValidationError() {
        UpdateLoanPolicyRequest request = loanTestCases.getUpdateLoanPolicyRequestTestCase();
        LoanPolicy existingLoanPolicy = loanTestCases.getLoanPolicyTestCase();

        when(loanPolicyQueryService.findById(request.getId())).thenReturn(existingLoanPolicy);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(loanPolicyValidator).validateUpdate(request, existingLoanPolicy);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanPolicyService.update(request)
        );

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(loanPolicyQueryService).findById(request.getId());
        verify(loanPolicyValidator).validateUpdate(request, existingLoanPolicy);
        verify(loanPolicyQueryService, never()).save(any());
    }

    @Test
    public void getLoanPolicyByIdSuccess() {
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();
        GetLoanPolicyResponse response = loanTestCases.getLoanPolicyResponseTestCase();

        when(loanPolicyQueryService.findById(loanPolicy.getId())).thenReturn(loanPolicy);
        when(loanPolicyMapper.toDto(loanPolicy)).thenReturn(response);

        GetLoanPolicyResponse result = loanPolicyService.getById(loanPolicy.getId());

        Assertions.assertEquals(response, result);
        verify(loanPolicyQueryService).findById(loanPolicy.getId());
        verify(loanPolicyMapper).toDto(loanPolicy);
    }

    @Test
    public void getLoanPolicyByIdFailureNotFound() {
        when(loanPolicyQueryService.findById(999L))
                .thenThrow(new NotFoundException("Loan policy not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> loanPolicyService.getById(999L)
        );

        Assertions.assertEquals("Loan policy not found with id: 999", exception.getMessage());
        verify(loanPolicyQueryService).findById(999L);
        verify(loanPolicyMapper, never()).toDto(any());
    }

    @Test
    public void getLoanPolicyByPageSuccess() {
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();
        GetLoanPolicyResponse response = loanTestCases.getLoanPolicyResponseTestCase();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        Page<LoanPolicy> page = new PageImpl<>(List.of(loanPolicy));

        when(loanPolicyQueryService.findAllWithPagination(paginationDto)).thenReturn(page);
        when(loanPolicyMapper.toDtoList(List.of(loanPolicy))).thenReturn(List.of(response));

        List<GetLoanPolicyResponse> result = loanPolicyService.getByPage(paginationDto);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(loanPolicyQueryService).findAllWithPagination(paginationDto);
        verify(loanPolicyMapper).toDtoList(List.of(loanPolicy));
    }

    @Test
    public void getLoanPolicyByPageFailureInvalidPagination() {
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(-1);
        paginationDto.setLimit(10);

        when(loanPolicyQueryService.findAllWithPagination(paginationDto))
                .thenThrow(new IllegalArgumentException("Page index must not be less than zero"));

        RuntimeException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> loanPolicyService.getByPage(paginationDto)
        );

        Assertions.assertEquals("Page index must not be less than zero", exception.getMessage());
        verify(loanPolicyQueryService).findAllWithPagination(paginationDto);
        verify(loanPolicyMapper, never()).toDtoList(any());
    }
}

