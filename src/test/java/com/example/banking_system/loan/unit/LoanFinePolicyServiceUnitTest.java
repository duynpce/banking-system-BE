package com.example.banking_system.loan.unit;

import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.loan.dto.CreateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFinePolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.mapper.LoanFinePolicyMapper;
import com.example.banking_system.domain.loan.service.domain.LoanFinePolicyService;
import com.example.banking_system.domain.loan.service.query.LoanFinePolicyQueryService;
import com.example.banking_system.domain.loan.validator.LoanFinePolicyValidator;
import com.example.banking_system.loan.LoanTestCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.Mockito.*;

public class LoanFinePolicyServiceUnitTest extends UnitTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();

    @Mock
    private LoanFinePolicyQueryService loanFinePolicyQueryService;

    @Mock
    private LoanFinePolicyMapper loanFinePolicyMapper;

    @Mock
    private LoanFinePolicyValidator loanFinePolicyValidator;

    @InjectMocks
    private LoanFinePolicyService loanFinePolicyService;

    @Test
    public void createLoanFinePolicySuccess() {
        CreateLoanFinePolicyRequest request = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        LoanFinePolicy loanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();
        LoanFinePolicy savedLoanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();

        when(loanFinePolicyMapper.toEntity(request)).thenReturn(loanFinePolicy);
        doNothing().when(loanFinePolicyValidator).validateCreate(loanFinePolicy);
        when(loanFinePolicyQueryService.save(loanFinePolicy)).thenReturn(savedLoanFinePolicy);

        LoanFinePolicy result = loanFinePolicyService.create(request);

        Assertions.assertEquals(savedLoanFinePolicy, result);
        verify(loanFinePolicyMapper).toEntity(request);
        verify(loanFinePolicyValidator).validateCreate(loanFinePolicy);
        verify(loanFinePolicyQueryService).save(loanFinePolicy);
    }

    @Test
    public void createLoanFinePolicyFailureValidationError() {
        CreateLoanFinePolicyRequest request = loanTestCases.getCreateLoanFinePolicyRequestTestCase();
        LoanFinePolicy loanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();

        when(loanFinePolicyMapper.toEntity(request)).thenReturn(loanFinePolicy);
        doThrow(new ValidationException("effective to date must be after effective from date"))
                .when(loanFinePolicyValidator).validateCreate(loanFinePolicy);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanFinePolicyService.create(request)
        );

        Assertions.assertEquals("effective to date must be after effective from date", exception.getMessage());
        verify(loanFinePolicyMapper).toEntity(request);
        verify(loanFinePolicyValidator).validateCreate(loanFinePolicy);
        verify(loanFinePolicyQueryService, never()).save(any());
    }

    @Test
    public void updateLoanFinePolicySuccess() {
        UpdateLoanFinePolicyRequest request = loanTestCases.getUpdateLoanFinePolicyRequestTestCase();
        LoanFinePolicy existingLoanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();
        LoanFinePolicy savedLoanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();

        when(loanFinePolicyQueryService.findById(request.getId())).thenReturn(existingLoanFinePolicy);
        doNothing().when(loanFinePolicyValidator).validateUpdate(request, existingLoanFinePolicy);
        when(loanFinePolicyQueryService.save(existingLoanFinePolicy)).thenReturn(savedLoanFinePolicy);

        LoanFinePolicy result = loanFinePolicyService.update(request);

        Assertions.assertEquals(savedLoanFinePolicy, result);
        verify(loanFinePolicyQueryService).findById(request.getId());
        verify(loanFinePolicyValidator).validateUpdate(request, existingLoanFinePolicy);
        verify(loanFinePolicyQueryService).save(existingLoanFinePolicy);
    }

    @Test
    public void updateLoanFinePolicyFailureValidationError() {
        UpdateLoanFinePolicyRequest request = loanTestCases.getUpdateLoanFinePolicyRequestTestCase();
        LoanFinePolicy existingLoanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();

        when(loanFinePolicyQueryService.findById(request.getId())).thenReturn(existingLoanFinePolicy);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(loanFinePolicyValidator).validateUpdate(request, existingLoanFinePolicy);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanFinePolicyService.update(request)
        );

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(loanFinePolicyQueryService).findById(request.getId());
        verify(loanFinePolicyValidator).validateUpdate(request, existingLoanFinePolicy);
        verify(loanFinePolicyQueryService, never()).save(any());
    }

    @Test
    public void getLoanFinePolicyByIdSuccess() {
        LoanFinePolicy loanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();
        GetLoanFinePolicyResponse response = loanTestCases.getLoanFinePolicyResponseTestCase();

        when(loanFinePolicyQueryService.findById(loanFinePolicy.getId())).thenReturn(loanFinePolicy);
        when(loanFinePolicyMapper.toDto(loanFinePolicy)).thenReturn(response);

        GetLoanFinePolicyResponse result = loanFinePolicyService.getById(loanFinePolicy.getId());

        Assertions.assertEquals(response, result);
        verify(loanFinePolicyQueryService).findById(loanFinePolicy.getId());
        verify(loanFinePolicyMapper).toDto(loanFinePolicy);
    }

    @Test
    public void getLoanFinePolicyByIdFailureNotFound() {
        when(loanFinePolicyQueryService.findById(999L))
                .thenThrow(new NotFoundException("Loan fine policy not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> loanFinePolicyService.getById(999L)
        );

        Assertions.assertEquals("Loan fine policy not found with id: 999", exception.getMessage());
        verify(loanFinePolicyQueryService).findById(999L);
        verify(loanFinePolicyMapper, never()).toDto(any());
    }

    @Test
    public void getLoanFinePolicyByPageSuccess() {
        LoanFinePolicy loanFinePolicy = loanTestCases.getLoanFinePolicyTestCase();
        GetLoanFinePolicyResponse response = loanTestCases.getLoanFinePolicyResponseTestCase();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        Page<LoanFinePolicy> page = new PageImpl<>(List.of(loanFinePolicy));

        when(loanFinePolicyQueryService.findAllWithPagination(paginationDto)).thenReturn(page);
        when(loanFinePolicyMapper.toDtoList(List.of(loanFinePolicy))).thenReturn(List.of(response));

        List<GetLoanFinePolicyResponse> result = loanFinePolicyService.getByPage(paginationDto);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(loanFinePolicyQueryService).findAllWithPagination(paginationDto);
        verify(loanFinePolicyMapper).toDtoList(List.of(loanFinePolicy));
    }

    @Test
    public void getLoanFinePolicyByPageFailureInvalidPagination() {
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(-1);
        paginationDto.setLimit(10);

        when(loanFinePolicyQueryService.findAllWithPagination(paginationDto))
                .thenThrow(new IllegalArgumentException("Page index must not be less than zero"));

        RuntimeException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> loanFinePolicyService.getByPage(paginationDto)
        );

        Assertions.assertEquals("Page index must not be less than zero", exception.getMessage());
        verify(loanFinePolicyQueryService).findAllWithPagination(paginationDto);
        verify(loanFinePolicyMapper, never()).toDtoList(any());
    }
}

