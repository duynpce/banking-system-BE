package com.example.banking_system.loan.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.loan.dto.CreateLoanFineRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFineResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFineRequest;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanFine;
import com.example.banking_system.domain.loan.mapper.LoanFineMapper;
import com.example.banking_system.domain.loan.service.domain.LoanFineService;
import com.example.banking_system.domain.loan.service.query.LoanFinePolicyQueryService;
import com.example.banking_system.domain.loan.service.query.LoanFineQueryService;
import com.example.banking_system.domain.loan.service.query.LoanQueryService;
import com.example.banking_system.domain.loan.validator.LoanFineValidator;
import com.example.banking_system.loan.LoanTestCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class LoanFineServiceUnitTest extends UnitTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    private LoanFineQueryService loanFineQueryService;

    @Mock
    private LoanQueryService loanQueryService;

    @Mock
    private LoanFineMapper loanFineMapper;

    @Mock
    private LoanFineValidator loanFineValidator;

    @Mock
    private LoanFinePolicyQueryService loanFinePolicyQueryService;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    AccountQueryService accountQueryService;

    @InjectMocks
    private LoanFineService loanFineService;

    @Test
    public void createLoanFineSuccess() {
        setupJwt();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Loan loan = loanTestCases.getLoanTestCase();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loan.getId(), accountId, 1);
        LoanFine loanFine = new LoanFine();
        LoanFine savedLoanFine = new LoanFine();

        when(loanFineMapper.toEntity(request)).thenReturn(loanFine);
        when(loanQueryService.findById(request.getLoanId())).thenReturn(loan);
        doNothing().when(loanFineValidator).validateCreate(loanFine);
        when(loanFineQueryService.save(loanFine)).thenReturn(savedLoanFine);
        when(loanFinePolicyQueryService.findById(anyLong())).thenReturn(loanTestCases.getLoanFinePolicyTestCase());
        when(accountQueryService.findById(accountId)).thenReturn(accountTestCases.getPersonalAccountTestCase().getAccount());

        LoanFine result = loanFineService.create(request);

        Assertions.assertEquals(savedLoanFine, result);
        Assertions.assertEquals(loan, loanFine.getLoan());
        verify(loanFineMapper).toEntity(request);
        verify(loanQueryService).findById(request.getLoanId());
        verify(loanFineValidator).validateCreate(loanFine);
        verify(loanFineQueryService).save(loanFine);
    }

    @Test
    public void createLoanFineFailureValidationError() {
        setupJwt();
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Loan loan = loanTestCases.getLoanTestCase();
        CreateLoanFineRequest request = loanTestCases.getCreateLoanFineRequestTestCase(loan.getId(), accountId, 1);
        LoanFine loanFine = new LoanFine();

        when(loanFineMapper.toEntity(request)).thenReturn(loanFine);
        when(loanQueryService.findById(request.getLoanId())).thenReturn(loan);
        doThrow(new ValidationException("amount must be greater than 0"))
                .when(loanFineValidator).validateCreate(loanFine);
        when(accountQueryService.findById(accountId)).thenReturn(accountTestCases.getPersonalAccountTestCase().getAccount());
        when(loanFinePolicyQueryService.findById(anyLong())).thenReturn(loanTestCases.getLoanFinePolicyTestCase());

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanFineService.create(request)
        );

        Assertions.assertEquals("amount must be greater than 0", exception.getMessage());
        verify(loanFineMapper).toEntity(request);
        verify(loanQueryService).findById(request.getLoanId());
        verify(loanFineValidator).validateCreate(loanFine);
        verify(loanFineQueryService, never()).save(any());
    }

    @Test
    public void updateLoanFineSuccess() {
        UpdateLoanFineRequest request = loanTestCases.getUpdateLoanFineRequestTestCase(1L);
        LoanFine existingLoanFine = new LoanFine();
        LoanFine savedLoanFine = new LoanFine();

        when(loanFineQueryService.findById(request.getId())).thenReturn(existingLoanFine);
        doNothing().when(loanFineValidator).validateUpdate(request, existingLoanFine);
        when(loanFineQueryService.save(existingLoanFine)).thenReturn(savedLoanFine);

        LoanFine result = loanFineService.update(request);

        Assertions.assertEquals(savedLoanFine, result);
        verify(loanFineQueryService).findById(request.getId());
        verify(loanFineValidator).validateUpdate(request, existingLoanFine);
        verify(loanFineQueryService).save(existingLoanFine);
    }

    @Test
    public void updateLoanFineFailureValidationError() {
        UpdateLoanFineRequest request = loanTestCases.getUpdateLoanFineRequestTestCase(1L);
        LoanFine existingLoanFine = new LoanFine();

        when(loanFineQueryService.findById(request.getId())).thenReturn(existingLoanFine);
        doThrow(new ValidationException("At least one field must be provided for update"))
                .when(loanFineValidator).validateUpdate(request, existingLoanFine);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanFineService.update(request)
        );

        Assertions.assertEquals("At least one field must be provided for update", exception.getMessage());
        verify(loanFineQueryService).findById(request.getId());
        verify(loanFineValidator).validateUpdate(request, existingLoanFine);
        verify(loanFineQueryService, never()).save(any());
    }

    @Test
    public void getLoanFineByIdSuccess() {
        Loan loan = loanTestCases.getLoanTestCase();
        LoanFine loanFine = loanTestCases.getLoanFineTestCase(loan);
        GetLoanFineResponse response = loanTestCases.getLoanFineResponseTestCase();

        when(loanFineQueryService.findById(loanFine.getId())).thenReturn(loanFine);
        when(loanFineMapper.toDto(loanFine)).thenReturn(response);

        GetLoanFineResponse result = loanFineService.getById(loanFine.getId());

        Assertions.assertEquals(response, result);
        verify(loanFineQueryService).findById(loanFine.getId());
        verify(loanFineMapper).toDto(loanFine);
    }

    @Test
    public void getLoanFineByIdFailureNotFound() {
        when(loanFineQueryService.findById(999L))
                .thenThrow(new NotFoundException("Loan fine not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> loanFineService.getById(999L)
        );

        Assertions.assertEquals("Loan fine not found with id: 999", exception.getMessage());
        verify(loanFineQueryService).findById(999L);
        verify(loanFineMapper, never()).toDto(any());
    }

    @Test
    public void getLoanFineByPageSuccess() {
        setupJwt();

        Loan loan = loanTestCases.getLoanTestCase();
        LoanFine loanFine = loanTestCases.getLoanFineTestCase(loan);
        GetLoanFineResponse response = loanTestCases.getLoanFineResponseTestCase();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        Page<LoanFine> page = new PageImpl<>(List.of(loanFine));

        when(loanFineQueryService.findByAccountIdWithPagination(anyLong(), eq(paginationDto))).thenReturn(page);
        when(loanFineMapper.toDtoList(List.of(loanFine))).thenReturn(List.of(response));

        List<GetLoanFineResponse> result = loanFineService.getByPage(paginationDto);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.getFirst());
        verify(loanFineQueryService).findByAccountIdWithPagination(anyLong(), eq(paginationDto));
        verify(loanFineMapper).toDtoList(List.of(loanFine));
    }

    private void setupJwt(){
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
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
    }

}

