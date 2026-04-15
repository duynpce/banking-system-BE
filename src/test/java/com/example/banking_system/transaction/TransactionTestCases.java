package com.example.banking_system.transaction;

import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionTestCases {

	private static TransactionTestCases instance;

	@Getter
	private final Transaction transactionTestCase = new Transaction();

	{
		transactionTestCase.setId(1L);
		transactionTestCase.setDescription("Transfer for testing");
		transactionTestCase.setTransferredAmount(new BigDecimal("100.00"));
		transactionTestCase.setType(TransactionType.TRANSFER);
		transactionTestCase.setStatus(TransactionStatus.COMPLETED);
		transactionTestCase.setCreatedAt(Instant.now());
	}
	public static TransactionTestCases getInstance() {
		if (instance == null) {
			instance = new TransactionTestCases();
		}

		return instance;
	}


	public CreateTransactionRequest getCreateTransferRequest(String receiverAccountNumber) {
		return buildCreateRequest(receiverAccountNumber, TransactionType.TRANSFER);
	}

	public CreateTransactionRequest getCreatePaymentRequest(String receiverAccountNumber) {
		return buildCreateRequest(receiverAccountNumber, TransactionType.PAYMENT);
	}

	public CreateTransactionRequest getCreateDepositRequest(String receiverAccountNumber) {
		return buildCreateRequest(receiverAccountNumber, TransactionType.DEPOSIT);
	}

	public CreateTransactionRequest getCreateWithdrawalRequest(String receiverAccountNumber) {
		return buildCreateRequest(receiverAccountNumber, TransactionType.WITHDRAWAL);
	}

	public CreateTransactionRequest getCreateInvalidReceiverRequest(String receiverAccountNumber, TransactionType type) {
		return buildCreateRequest(receiverAccountNumber, type);
	}

	private CreateTransactionRequest buildCreateRequest(String receiverAccountNumber, TransactionType type) {
		CreateTransactionRequest request = new CreateTransactionRequest();
		request.setReceiverAccountNumber(receiverAccountNumber);
		request.setDescription(type.name() + " for testing");
		request.setTransferredAmount(new BigDecimal("100.00"));
		request.setType(type);
		return request;
	}

}
