package com.example.banking_system.domain.transaction;

import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import jakarta.persistence.Column;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "senderAccountNumber", source = "sender.number")
    @Mapping(target = "receiverAccountNumber", source = "receiver.number")
    @Mapping(target = "postedBalance", expression = "java(resolvePostedBalance(transaction, username))")
    GetTransactionResponse toDto(Transaction transaction, @Context String username);

    //get postedBalance by username
    default BigDecimal resolvePostedBalance(Transaction transaction, String username) {
        if (transaction == null || username == null) {
            return null;
        }

        if (transaction.getSender() != null
                && username.equals(transaction.getSender().getUsername())) {
            return transaction.getSenderPostedBalance();
        }

        if (transaction.getReceiver() != null
                && username.equals(transaction.getReceiver().getUsername())) {
            return transaction.getReceiverPostedBalance();
        }

        return null;
    }

    List<GetTransactionResponse> toDtoList(List<Transaction> transactions, @Context String username);

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver.number", source = "receiverAccountNumber")
    Transaction toEntity(CreateTransactionRequest request);
}
