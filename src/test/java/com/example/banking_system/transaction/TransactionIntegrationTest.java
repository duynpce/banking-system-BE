package com.example.banking_system.transaction;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.controller.BusinessAccountController;
import com.example.banking_system.domain.account.controller.PersonalAccountController;
import com.example.banking_system.domain.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.domain.account.dto.CreatePersonalAccountRequest;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.TransactionController;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Sql(scripts = "/insert-internal-account.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransactionIntegrationTest extends IntegrationTest {

	private final TransactionTestCases transactionTestCases = TransactionTestCases.getInstance();
	private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

	@Autowired
	private TransactionController transactionController;

	@Autowired
	private PersonalAccountController personalAccountController;

	@Autowired
	private BusinessAccountController businessAccountController;

	@Autowired
	private AccountQueryService accountQueryService;

	@MockitoBean
	private JwtUtil jwtUtil;

	@Test
	public void createTransactionSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		CreateTransactionRequest request = transferScenario.request();
		ResponseEntity<ResponseDto<String>> response = transactionController.create(request);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
		assertNotNull(response.getBody(), "Response body should not be null");
		assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
		assertEquals("Transaction created successfully", response.getBody().getMessage(), "Success message should match");

		//because in transaction not commited yet , so this object updated by hibernate (it's like query updated object)
		assertEquals(transferScenario.receiver().getBalance(), transferScenario.initialBalance().add(request.getTransferredAmount()), "Receiver balance should be increased by transferred amount");
		assertEquals(transferScenario.sender().getBalance(), transferScenario.initialBalance().subtract(request.getTransferredAmount()), "Sender balance should be decreased by transferred amount");


	}

	@Test
	public void createTransactionFailureValidationError() {
		setupTransferScenario();

		// create request with  internal account number(not allow to make transaction with)
		CreateTransactionRequest request = transactionTestCases.getCreateTransferRequest(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER());

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
	}

	@Test
	public void createPaymentTransactionSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		CreateTransactionRequest request = transactionTestCases.getCreatePaymentRequest(transferScenario.receiver().getNumber());

		ResponseEntity<ResponseDto<String>> response = transactionController.create(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());

		when(jwtUtil.getUsername()).thenReturn(transferScenario.sender().getUsername());
		ResponseEntity<ResponseDto<List<GetTransactionResponse>>> getResponse = transactionController.getByPage(0, 10);
		assertNotNull(getResponse.getBody());
		GetTransactionResponse result = getResponse.getBody().getData().getFirst();

		assertEquals(request.getType(), result.getType());
		assertEquals(request.getTransferredAmount(), result.getTransferredAmount());
		assertEquals(transferScenario.sender().getNumber(), result.getSenderAccountNumber());
		assertEquals(transferScenario.receiver().getNumber(), result.getReceiverAccountNumber());
		assertNull(result.getPostedBalance());
	}

	@Test
	public void createPaymentTransactionFailureValidationError() {
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest(
				accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER(),
				com.example.banking_system.domain.transaction.constant.TransactionType.PAYMENT
		);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
	}

	@Test
	public void createDepositTransactionSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		Account account = transferScenario.receiver();
		Account internalDepositAccount = accountQueryService.getInternalDePositAccount();
		BigDecimal initialAccountBalance = account.getBalance();
		BigDecimal initialInternalBalance = internalDepositAccount.getBalance();
		CreateTransactionRequest request = transactionTestCases.getCreateDepositRequest(account.getNumber());

		ResponseEntity<ResponseDto<String>> response = transactionController.create(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(initialAccountBalance.add(request.getTransferredAmount()), account.getBalance());
		assertEquals(initialInternalBalance.subtract(request.getTransferredAmount()), internalDepositAccount.getBalance());
	}

	@Test
	public void createDepositTransactionFailureValidationError() {
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest(
				accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER(),
				com.example.banking_system.domain.transaction.constant.TransactionType.DEPOSIT
		);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
	}

	@Test
	public void createWithdrawalTransactionSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		Account account = transferScenario.sender();
		Account internalWithdrawalAccount = accountQueryService.getInternalWithdrawalAccount();
		BigDecimal initialAccountBalance = account.getBalance();
		BigDecimal initialInternalBalance = internalWithdrawalAccount.getBalance();
		CreateTransactionRequest request = transactionTestCases.getCreateWithdrawalRequest(transferScenario.receiver().getNumber());

		ResponseEntity<ResponseDto<String>> response = transactionController.create(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(initialAccountBalance.subtract(request.getTransferredAmount()), account.getBalance());
		assertEquals(initialInternalBalance.add(request.getTransferredAmount()), internalWithdrawalAccount.getBalance());
	}

	@Test
	public void createWithdrawalTransactionFailureValidationError() {
		CreateTransactionRequest request = transactionTestCases.getCreateInvalidReceiverRequest(
				accountQueryService.getINTERNAL_WITHDRAWAL_ACCOUNT_NUMBER(),
				com.example.banking_system.domain.transaction.constant.TransactionType.WITHDRAWAL
		);

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.create(request)
		);

		assertEquals("Invalid receiver account number", exception.getMessage());
	}

	@Test
	public void getByPageSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		CreateTransactionRequest request = transferScenario.request();
		transactionController.create(request);

		when(jwtUtil.getUsername()).thenReturn(transferScenario.sender().getUsername());
		ResponseEntity<ResponseDto<List<GetTransactionResponse>>> response = transactionController.getByPage(0, 10);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
		assertNotNull(response.getBody(), "Response body should not be null");
		assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
		assertNotNull(response.getBody().getData(), "Response data should not be null");
		assertEquals(1, response.getBody().getData().size(), "Response data size should be 1");

		GetTransactionResponse result   = response.getBody().getData().getFirst();

		assertEquals(request.getTransferredAmount(), result.getTransferredAmount(), "Transferred amount should match");
		assertEquals(request.getDescription(), result.getDescription(), "Description should match");
		assertEquals(transferScenario.sender().getNumber(), result.getSenderAccountNumber(), "Sender account number should match");
		assertEquals(transferScenario.receiver().getNumber(), result.getReceiverAccountNumber(), "Receiver account number should match");
		assertEquals(transferScenario.initialBalance().subtract(request.getTransferredAmount()), result.getPostedBalance(), "Posted balance should match sender balance after transfer");
	}

	@Test
	public void getByPageFailureValidationError() {
		when(jwtUtil.getUsername()).thenReturn("test_user");

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.getByPage(-1, 10)
		);

		assertEquals("page must be greater than or equal to 0", exception.getMessage());
	}

	@Test
	public void getByDateRangeSuccess() {
		TransferScenario transferScenario = setupTransferScenario();
		CreateTransactionRequest request = transferScenario.request();
		transactionController.create(request);

		when(jwtUtil.getUsername()).thenReturn(transferScenario.sender().getUsername());
		ResponseEntity<ResponseDto<List<GetTransactionResponse>>> response = transactionController.getByDateRange(
				LocalDate.now().minusDays(1),
				LocalDate.now().plusDays(1)
		);

		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
		assertNotNull(response.getBody(), "Response body should not be null");
		assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
		assertNotNull(response.getBody().getData(), "Response data should not be null");
		assertFalse(response.getBody().getData().isEmpty(), "Response data should not be empty");

		GetTransactionResponse result = response.getBody().getData().getFirst();
		assertEquals(request.getTransferredAmount(), result.getTransferredAmount(), "Transferred amount should match");
		assertEquals(request.getDescription(), result.getDescription(), "Description should match");
		assertEquals(transferScenario.sender().getNumber(), result.getSenderAccountNumber(), "Sender account number should match");
		assertEquals(transferScenario.receiver().getNumber(), result.getReceiverAccountNumber(), "Receiver account number should match");
		assertEquals(transferScenario.initialBalance().subtract(request.getTransferredAmount()), result.getPostedBalance(), "Posted balance should match sender balance after transfer");
	}

	@Test
	public void getByDateRangeFailureValidationError() {
		when(jwtUtil.getUsername()).thenReturn("test_user");

		ValidationException exception = Assertions.assertThrows(
				ValidationException.class,
				() -> transactionController.getByDateRange(LocalDate.now(), LocalDate.now().minusDays(1))
		);

		assertEquals("startDate must be before or equal to endDate", exception.getMessage());
	}

	private TransferScenario setupTransferScenario() {
		CreatePersonalAccountRequest createPersonalAccountRequest = accountTestCases.getCreatePersonalAccountRequestTestCase();
		CreateBusinessAccountRequest createBusinessAccountRequest = accountTestCases.getCreateBusinessAccountRequestTestCase();

		personalAccountController.create(createPersonalAccountRequest);
		businessAccountController.create(createBusinessAccountRequest);

		Account sender = accountQueryService.findByUsername(createBusinessAccountRequest.getUsername());
		Account receiver = accountQueryService.findByUsername(createPersonalAccountRequest.getUsername());

		BigDecimal initialBalance = new BigDecimal("500.00");
		sender.setBalance(initialBalance);
		receiver.setBalance(initialBalance);

		accountQueryService.save(receiver);
		accountQueryService.save(sender);

		Jwt jwt = new Jwt(
				"test-token",
				Instant.now(),
				Instant.now().plusSeconds(3600),
				Map.of("alg", "none"),
				Map.of(
						"account_id", sender.getId(),
						"account_number", sender.getNumber()
				)
		);
		when(jwtUtil.getJwtClaims()).thenReturn(jwt);

		CreateTransactionRequest request = transactionTestCases.getCreateTransferRequest(receiver.getNumber());
		return new TransferScenario(sender, receiver, initialBalance, request);
	}

	private record TransferScenario(
			Account sender,
			Account receiver,
			BigDecimal initialBalance,
			CreateTransactionRequest request
	) {
	}


}
