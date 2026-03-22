package com.example.banking_system.domain.transaction;

import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "fromAccountNumber", source = "fromAccount.accountNumber")
    @Mapping(target = "toAccountNumber", source = "toAccount.accountNumber")
    GetTransactionResponse toDto(Transaction transaction);

    List<GetTransactionResponse> toDtoList(List<Transaction> transactions);
}
