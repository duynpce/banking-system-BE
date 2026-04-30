package com.example.banking_system.loan.unit;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.dto.LoanFilter;
import com.example.banking_system.domain.loan.dto.RepayLoanRequest;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.mapper.LoanMapper;
import com.example.banking_system.domain.loan.service.domain.LoanService;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
import com.example.banking_system.domain.loan.service.query.LoanQueryService;
import com.example.banking_system.domain.loan.validator.LoanValidator;
import com.example.banking_system.domain.transaction.service.TransactionQueryService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LoanUnitTest extends UnitTest {

    private final LoanTestCases loanTestCases = LoanTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Mock
    private LoanQueryService loanQueryService;

    @Mock
    private LoanPolicyQueryService loanPolicyQueryService;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private TransactionQueryService transactionQueryService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanValidator loanValidator;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LoanService loanService;

    @Test
    public void createLoanSuccess() {
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();
        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase(loanPolicy.getId(), loanPolicy.getLoanType());
        Loan loan = new Loan();
        loan.setTotalAmount(request.getAmount());
        BigDecimal initialBalance = new BigDecimal("500.00");
        account.setBalance(initialBalance);

        setupJwt();
        when(loanMapper.toEntity(request)).thenReturn(loan);
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(loanPolicyQueryService.findById(request.getPolicyId())).thenReturn(loanPolicy);
        doNothing().when(loanValidator).validateCreate(loan, loanPolicy);
        when(loanQueryService.save(loan)).thenReturn(loan);

        Loan result = loanService.create(request);

        assertEquals(loan, result);
        assertEquals(initialBalance.add(request.getAmount()), account.getBalance());
        assertEquals(LocalDate.now().plusMonths(loanPolicy.getDurationMonths()), loan.getDueDate());
        assertEquals(loanPolicy, loan.getPolicy());
        assertEquals(account, loan.getAccount());
        verify(loanValidator).validateCreate(loan, loanPolicy);
        verify(loanQueryService).save(loan);
    }

    @Test
    public void createLoanFailureValidationError() {
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
        LoanPolicy loanPolicy = loanTestCases.getLoanPolicyTestCase();
        CreateLoanRequest request = loanTestCases.getCreateLoanRequestTestCase(loanPolicy.getId(), loanPolicy.getLoanType());
        Loan loan = new Loan();

        setupJwt();
        when(loanMapper.toEntity(request)).thenReturn(loan);
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(loanPolicyQueryService.findById(request.getPolicyId())).thenReturn(loanPolicy);
        doThrow(new ValidationException("loan amount cannot be greater than maximum allowed by policy"))
                .when(loanValidator).validateCreate(loan, loanPolicy);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanService.create(request)
        );

        assertEquals("loan amount cannot be greater than maximum allowed by policy", exception.getMessage());
        verify(loanValidator).validateCreate(loan, loanPolicy);
        verify(loanQueryService, never()).save(any());
    }

    @Test
    public void getLoanByIdSuccess() {
        Loan loan = loanTestCases.getLoanTestCase();
        GetLoanResponse response = loanTestCases.getLoanResponseTestCase();

        when(loanQueryService.findById(loan.getId())).thenReturn(loan);
        when(loanMapper.toDto(loan)).thenReturn(response);

        GetLoanResponse result = loanService.getById(loan.getId());

        assertEquals(response, result);
        verify(loanQueryService).findById(loan.getId());
        verify(loanMapper).toDto(loan);
    }

    @Test
    public void getLoanByIdFailureNotFound() {
        when(loanQueryService.findById(999L))
                .thenThrow(new NotFoundException("Loan not found with id: 999"));

        RuntimeException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> loanService.getById(999L)
        );

        assertEquals("Loan not found with id: 999", exception.getMessage());
        verify(loanQueryService).findById(999L);
        verify(loanMapper, never()).toDto(any());
    }

    @Test
    public void getLoanByPageSuccess() {
        Loan loan = loanTestCases.getLoanTestCase();
        GetLoanResponse response = loanTestCases.getLoanResponseTestCase();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        Page<Loan> page = new PageImpl<>(List.of(loan));

        setupJwt();
        when(loanQueryService.findByAccountIdWithPagination(anyLong(), eq(paginationDto))).thenReturn(page);
        when(loanMapper.toDtoList(List.of(loan))).thenReturn(List.of(response));

        List<GetLoanResponse> result = loanService.getByPage(paginationDto);

        assertEquals(1, result.size());
        assertEquals(response, result.getFirst());
        verify(loanQueryService).findByAccountIdWithPagination(anyLong(), eq(paginationDto));
        verify(loanMapper).toDtoList(List.of(loan));
    }

    @Test
    public void getByFilterSuccess() {
        Loan loan = loanTestCases.getLoanTestCase();
        GetLoanResponse response = loanTestCases.getLoanResponseTestCase();
        LoanFilter loanFilter = loanTestCases.getLoanFilterTestCase();
        Page<Loan> page = new PageImpl<>(List.of(loan));

        setupJwt();
        when(loanQueryService.findByFilter(anyLong(), eq(loanFilter))).thenReturn(page);
        when(loanMapper.toDtoList(List.of(loan))).thenReturn(List.of(response));

        List<GetLoanResponse> result = loanService.getByFilter(loanFilter);

        assertEquals(1, result.size());
        assertEquals(response, result.getFirst());
        verify(loanQueryService).findByFilter(anyLong(), eq(loanFilter));
        verify(loanMapper).toDtoList(List.of(loan));
    }

    @Test
    public void getByFilterEmptyResult() {
        LoanFilter loanFilter = loanTestCases.getLoanFilterTestCase();
        loanFilter.setStatus(LoanStatus.DONE_PAYMENT);
        Page<Loan> emptyPage = new PageImpl<>(List.of());

        setupJwt();
        when(loanQueryService.findByFilter(anyLong(), eq(loanFilter))).thenReturn(emptyPage);
        when(loanMapper.toDtoList(List.of())).thenReturn(List.of());

        List<GetLoanResponse> result = loanService.getByFilter(loanFilter);

        assertTrue(result.isEmpty());
        verify(loanQueryService).findByFilter(anyLong(), eq(loanFilter));
    }

    @Test
    public void getByReportsSuccess() {
        GetLoanReportResponse  response = loanTestCases.getLoanReportTestCase();
        LoanStatus loanStatus = LoanStatus.CURRENT_PAYMENT;

        setupJwt();
        when(loanQueryService.findReportByAccountIdAndStatus(anyLong(), eq(loanStatus))).thenReturn(response);

        GetLoanReportResponse result = loanService.getByReports(loanStatus);

        assertEquals(loanStatus, result.getLoanStatus());
        assertEquals(response.getTotalAmount(), result.getTotalAmount());
        assertEquals(response.getLeftAmount(), result.getLeftAmount());
        assertEquals(response.getMonthlyInstallment(), result.getMonthlyInstallment());
        verify(loanQueryService).findReportByAccountIdAndStatus(anyLong(), eq(loanStatus));
    }

    @Test
    public void getByReportsNoLoansReturnsZeroAmounts() {
        LoanStatus loanStatus = LoanStatus.DONE_PAYMENT;
        GetLoanReportResponse zeroResponse = new GetLoanReportResponse(loanStatus, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        setupJwt();
        when(loanQueryService.findReportByAccountIdAndStatus(anyLong(), eq(loanStatus))).thenReturn(zeroResponse);

        GetLoanReportResponse result = loanService.getByReports(loanStatus);

        assertEquals(loanStatus, result.getLoanStatus());
        assertEquals(0, result.getTotalAmount().compareTo(java.math.BigDecimal.ZERO));
        assertEquals(0, result.getLeftAmount().compareTo(java.math.BigDecimal.ZERO));
        assertEquals(0, result.getMonthlyInstallment().compareTo(java.math.BigDecimal.ZERO));
    }

    @Test
    public void repayLoanSuccess() {
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
        account.setBalance(new BigDecimal("1000.00"));
        Loan loan = loanTestCases.getLoanTestCase(account, loanTestCases.getLoanPolicyTestCase());
        loan.setLeftAmount(new BigDecimal("250.00"));
        RepayLoanRequest request = loanTestCases.getRepayLoanRequestTestCase(loan.getId(), new BigDecimal("250.00"));

        setupJwt();
        when(loanQueryService.findByIdAndAccountId(request.getLoanId(), account.getId())).thenReturn(loan);
        doNothing().when(loanValidator).validateRepay(loan, request, account);

        loanService.repayLoan(request);

        assertEquals(new BigDecimal("750.00"), account.getBalance());
        assertEquals(0, loan.getLeftAmount().compareTo(BigDecimal.ZERO));
        assertEquals(LoanStatus.DONE_PAYMENT, loan.getStatus());
        verify(accountQueryService).save(account);
        verify(loanQueryService).save(loan);
        verify(transactionQueryService).save(any());
    }

    @Test
    public void repayLoanFailureValidationError() {
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
        Loan loan = loanTestCases.getLoanTestCase();
        loan.setAccount(account);
        RepayLoanRequest request = loanTestCases.getRepayLoanRequestTestCase(loan.getId(), new BigDecimal("250.00"));
        setupJwt();

        doThrow(new ValidationException("loan is already fully repaid"))
                .when(loanValidator).validateRepay(loan, request, account);
        when(loanQueryService.findByIdAndAccountId(request.getLoanId(), account.getId())).thenReturn(loan).thenReturn(loan);

        RuntimeException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> loanService.repayLoan(request)
        );

        assertEquals("loan is already fully repaid", exception.getMessage());
        verify(accountQueryService, never()).save(any());
        verify(loanQueryService, never()).save(any());
        verify(transactionQueryService, never()).save(any());
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
