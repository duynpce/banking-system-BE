package com.example.banking_system.transaction;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.common.utility.TimeUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionMapper;
import com.example.banking_system.domain.transaction.TransactionRepository;
import com.example.banking_system.domain.transaction.TransactionValidator;
import com.example.banking_system.domain.transaction.constant.TransactionGroup;
import com.example.banking_system.domain.transaction.constant.TransactionReportType;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionReport;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import com.example.banking_system.domain.transaction.dto.TransactionFilter;
import com.example.banking_system.domain.transaction.dto.TransactionReportFilter;
import com.example.banking_system.domain.transaction.service.TransactionQueryService;
import com.example.banking_system.domain.transaction.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionUnitTest extends UnitTest {

	private final TransactionTestCases transactionTestCases = TransactionTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

	@Mock
	private TransactionQueryService transactionQueryService;

	@Mock
	private TransactionMapper transactionMapper;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TransactionValidator transactionValidator;

	@Mock
	private AccountQueryService accountQueryService;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private TimeUtil timeUtil;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	public void createTransactionSuccess() {

		Account sender = accountTestCases.getPersonalAccountTestCase().getAccount();
        Account receiver = accountTestCases.getBusinessAccountTestCase().getAccount();
        CreateTransactionRequest request = transactionTestCases.getCreateTransferRequest(receiver.getNumber());
        BigDecimal initialBalance = BigDecimal.valueOf(500);
        receiver.setBalance(initialBalance);
        sender.setBalance(initialBalance);

		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.TRANSFER);
		transaction.setTransferredAmount(request.getTransferredAmount());
		transaction.setReceiver(receiver);

		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(sender.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		when(accountQueryService.findById(sender.getId())).thenReturn(sender);
		when(accountQueryService.findByAccountNumber(receiver.getNumber())).thenReturn(receiver);
		when(transactionRepository.save(transaction)).thenReturn(transaction);

		Transaction result = transactionService.create(request);

		assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        assertEquals(transaction.getTransferredAmount(), request.getTransferredAmount());
        assertEquals(transaction.getReceiverPostedBalance(), receiver.getBalance());
		assertEquals(receiver.getBalance(), initialBalance.add(request.getTransferredAmount()));
        assertEquals(sender.getBalance(), initialBalance.subtract(request.getTransferredAmount()));

		verify(transactionValidator).validateCreate(request, sender);
		verify(accountQueryService).save(sender);
		verify(accountQueryService).save(receiver);
		verify(transactionRepository).save(transaction);
	}

	@Test
	public void createPaymentTransactionSuccess() {
		Account sender = accountTestCases.getPersonalAccountTestCase().getAccount();
		Account receiver = accountTestCases.getBusinessAccountTestCase().getAccount();
		CreateTransactionRequest request = transactionTestCases.getCreatePaymentRequest(receiver.getNumber());

		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.PAYMENT);
		transaction.setTransferredAmount(request.getTransferredAmount());
		transaction.setReceiver(receiver);

		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(sender.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		when(accountQueryService.findById(sender.getId())).thenReturn(sender);
		when(accountQueryService.findByAccountNumber(receiver.getNumber())).thenReturn(receiver);
		when(transactionRepository.save(transaction)).thenReturn(transaction);

		Transaction result = transactionService.create(request);

		assertEquals(TransactionStatus.PENDING, result.getStatus());
		assertEquals(sender, result.getSender());
		assertEquals(receiver, result.getReceiver());
		verify(transactionValidator).validateCreate(request, sender);
		verify(transactionRepository).save(transaction);
	}

	@Test
	public void createPaymentTransactionFailureValidationError() {
		Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest("123456789012", TransactionType.PAYMENT);


		Transaction transaction = new Transaction();

		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(account.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		doThrow(new ValidationException("Invalid receiver account number"))
				.when(transactionValidator).validateCreate(request, null);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
		verify(transactionValidator).validateCreate(request, null);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	public void createDepositTransactionSuccess() {
		Account account = accountTestCases.getBusinessAccountTestCase().getAccount();
		Account internalDepositAccount = accountTestCases.getGovernmentAccountTestCase().getAccount();
		CreateTransactionRequest request = transactionTestCases.getCreateDepositRequest(account.getNumber());

		BigDecimal accountInitialBalance = BigDecimal.valueOf(500);
		BigDecimal internalInitialBalance = BigDecimal.valueOf(2000);
		account.setBalance(accountInitialBalance);
		internalDepositAccount.setBalance(internalInitialBalance);

		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setTransferredAmount(request.getTransferredAmount());
		transaction.setReceiver(account);

		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(internalDepositAccount.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		when(accountQueryService.findByAccountNumber(account.getNumber())).thenReturn(account);
		when(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER()).thenReturn(internalDepositAccount.getNumber());
		when(accountQueryService.getInternalDePositAccount()).thenReturn(internalDepositAccount);
		when(transactionRepository.save(transaction)).thenReturn(transaction);
		when(accountQueryService.findById(internalDepositAccount.getId())).thenReturn(internalDepositAccount);

		Transaction result = transactionService.create(request);

		assertEquals(accountInitialBalance.add(request.getTransferredAmount()), account.getBalance());
		assertEquals(internalInitialBalance.subtract(request.getTransferredAmount()), internalDepositAccount.getBalance());
		assertEquals(account, result.getReceiver());
		assertEquals(account.getBalance(), result.getReceiverPostedBalance());
		verify(transactionValidator).validateCreate(request, internalDepositAccount);
		verify(accountQueryService).save(account);
		verify(accountQueryService).save(internalDepositAccount);
		verify(transactionRepository).save(transaction);
	}

	@Test
	public void createDepositTransactionFailureValidationError() {
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest("123456789012", TransactionType.DEPOSIT);
		Transaction transaction = new Transaction();
		Account account = accountTestCases.getBusinessAccountTestCase().getAccount();


		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(account.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		doThrow(new ValidationException("Invalid receiver account number"))
				.when(transactionValidator).validateCreate(request, null);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
		verify(transactionValidator).validateCreate(request, null);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	public void createWithdrawalTransactionSuccess() {
		Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
		Account internalWithdrawalAccount = accountTestCases.getGovernmentAccountTestCase().getAccount();
		CreateTransactionRequest request = transactionTestCases.getCreateWithdrawalRequest("123456789012");

		BigDecimal accountInitialBalance = BigDecimal.valueOf(500);
		BigDecimal internalInitialBalance = BigDecimal.valueOf(1000);
		account.setBalance(accountInitialBalance);
		internalWithdrawalAccount.setBalance(internalInitialBalance);

		Transaction transaction = new Transaction();
		transaction.setType(TransactionType.WITHDRAWAL);
		transaction.setTransferredAmount(request.getTransferredAmount());

		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(account.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		when(accountQueryService.findById(account.getId())).thenReturn(account);
		when(accountQueryService.getInternalWithdrawalAccount()).thenReturn(internalWithdrawalAccount);
		when(transactionRepository.save(transaction)).thenReturn(transaction);

		Transaction result = transactionService.create(request);

		assertEquals(accountInitialBalance.subtract(request.getTransferredAmount()), account.getBalance());
		assertEquals(internalInitialBalance.add(request.getTransferredAmount()), internalWithdrawalAccount.getBalance());
		assertEquals(account, result.getSender());
		assertEquals(account.getBalance(), result.getSenderPostedBalance());
		verify(transactionValidator).validateCreate(request, account);
		verify(accountQueryService).save(account);
		verify(accountQueryService).save(internalWithdrawalAccount);
		verify(transactionRepository).save(transaction);
	}

	@Test
	public void createWithdrawalTransactionFailureValidationError() {
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest("123456789012", TransactionType.WITHDRAWAL);
		Transaction transaction = new Transaction();
		Account account = accountTestCases.getBusinessAccountTestCase().getAccount();


		Jwt jwt = mock(Jwt.class);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(account.getId());
		when(transactionMapper.toEntity(request)).thenReturn(transaction);
		doThrow(new ValidationException("Invalid receiver account number"))
				.when(transactionValidator).validateCreate(request, null);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
		verify(transactionValidator).validateCreate(request, null);
		verify(transactionRepository, never()).save(any());
	}

	@Test
	public void getByFilterAllSuccess() {
		String username = "test_user";
		TransactionFilter transactionFilter = createFilter(TransactionGroup.ALL, 0, 10);

		Transaction transaction = transactionTestCases.getTransactionTestCase();
		List<Transaction> transactions = List.of(transaction);
		Page<Transaction> transactionPage = new PageImpl<>(transactions);

		GetTransactionResponse response = new GetTransactionResponse();
		response.setId(10L);
		List<GetTransactionResponse> responses = List.of(response);

		when(jwtUtil.getUsername()).thenReturn(username);
		when(transactionQueryService.findByFilter(username, transactionFilter)).thenReturn(transactionPage);
		when(transactionMapper.toDtoList(transactions, username)).thenReturn(responses);

		ResponseDto<List<GetTransactionResponse>> result = transactionService.getByFilter(transactionFilter);

		assertEquals(1, result.getData().size());
		assertEquals(10L, result.getData().getFirst().getId());
		assertEquals(1, result.getMetaData().getTotalItems());
		assertEquals(1, result.getMetaData().getTotalPages());
		assertEquals(0, result.getMetaData().getCurrentPage());
		assertEquals(10, result.getMetaData().getPageSize());
		verify(transactionQueryService).findByFilter(username, transactionFilter);
		verify(transactionMapper).toDtoList(transactions, username);
	}

	@Test
	public void getByFilterIncomeSuccess() {
		String username = "test_user";
		TransactionFilter transactionFilter = createFilter(TransactionGroup.INCOME, 0, 10);

		Transaction transaction = transactionTestCases.getTransactionTestCase();
		List<Transaction> transactions = List.of(transaction);
		Page<Transaction> transactionPage = new PageImpl<>(transactions);

		GetTransactionResponse response = new GetTransactionResponse();
		response.setId(20L);
		List<GetTransactionResponse> responses = List.of(response);

		when(jwtUtil.getUsername()).thenReturn(username);
		when(transactionQueryService.findByFilter(username, transactionFilter)).thenReturn(transactionPage);
		when(transactionMapper.toDtoList(transactions, username)).thenReturn(responses);

		ResponseDto<List<GetTransactionResponse>> result = transactionService.getByFilter(transactionFilter);

		assertEquals(1, result.getData().size());
		assertEquals(20L, result.getData().getFirst().getId());
		verify(transactionQueryService).findByFilter(username, transactionFilter);
		verify(transactionMapper).toDtoList(transactions, username);
	}

	@Test
	public void getByFilterFailureValidationErrorTransactionGroupRequired() {
		String username = "test_user";
		TransactionFilter transactionFilter = createFilter(null, 0, 10);

		when(jwtUtil.getUsername()).thenReturn(username);
		when(transactionQueryService.findByFilter(username, transactionFilter))
				.thenThrow(new ValidationException("transaction group is required"));

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.getByFilter(transactionFilter)
		);

		assertEquals("transaction group is required", exception.getMessage());
		verify(transactionQueryService).findByFilter(username, transactionFilter);
		verify(transactionMapper, never()).toDtoList(anyList(), anyString());
	}

	@Test
	public void getByFilterFailureValidationErrorDateRange() {
		String username = "test_user";
		TransactionFilter transactionFilter = createFilter(TransactionGroup.ALL, 0, 10);
		transactionFilter.setStartDate(LocalDate.now());
		transactionFilter.setEndDate(LocalDate.now().minusDays(1));

		when(jwtUtil.getUsername()).thenReturn(username);
		when(transactionQueryService.findByFilter(username, transactionFilter))
				.thenThrow(new ValidationException("startDate must be before or equal to endDate"));

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.getByFilter(transactionFilter)
		);

		assertEquals("startDate must be before or equal to endDate", exception.getMessage());
		verify(transactionQueryService).findByFilter(username, transactionFilter);
		verify(transactionMapper, never()).toDtoList(anyList(), anyString());
	}

	private TransactionFilter createFilter(TransactionGroup transactionGroup, int page, int limit) {
		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setPage(page);
		paginationDto.setLimit(limit);

		TransactionFilter transactionFilter = new TransactionFilter();
		transactionFilter.setPaginationDto(paginationDto);
		transactionFilter.setTransactionGroup(transactionGroup);
		return transactionFilter;
	}

	@Test
	public void getReportsYearSuccess() {
		TransactionReportFilter filter = transactionTestCases.getYearTransactionReportFilter();
		Jwt jwt = mock(Jwt.class);
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		LocalDate endDate = LocalDate.of(2026, 12, 31);

		GetTransactionReport report = new GetTransactionReport();
		report.setReportType(TransactionReportType.MONTH);
		report.setStartDate(LocalDate.of(2026, 1, 1));
		report.setEndDate(LocalDate.of(2026, 1, 31));

		when(jwtUtil.getJwtClaims()).thenReturn(jwt);
		when(jwt.getClaim("account_id")).thenReturn(1L);
		when(timeUtil.getYearStartDate(filter.getYear())).thenReturn(startDate);
		when(timeUtil.getYearEndDate(filter.getYear())).thenReturn(endDate);
		when(transactionQueryService.getTransactionReportByAccountAndDateRange(
				1L,
				startDate,
				endDate,
				"month",
				TransactionReportType.MONTH
		)).thenReturn(List.of(report));

		ResponseDto<List<GetTransactionReport>> response = transactionService.getReports(filter);

		assertEquals(1, response.getData().size());
		assertEquals(TransactionReportType.MONTH, response.getData().getFirst().getReportType());
		verify(transactionQueryService).getTransactionReportByAccountAndDateRange(
				1L,
				startDate,
				endDate,
				"month",
				TransactionReportType.MONTH
		);
	}

	@Test
	public void getReportsYearFailureValidationError() {
		TransactionReportFilter filter = transactionTestCases.getInvalidYearTransactionReportFilter();

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionService.getReports(filter)
		);

		assertEquals("year is required for year report", exception.getMessage());
		verify(transactionQueryService, never()).getTransactionReportByAccountAndDateRange(
				anyLong(), any(), any(), anyString(), any()
		);
	}
}
