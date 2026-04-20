package com.example.banking_system.domain.transaction;

import com.example.banking_system.common.dto.MetaDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import com.example.banking_system.domain.transaction.dto.TransactionFilter;
import com.example.banking_system.domain.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@RequestBody CreateTransactionRequest request) {
        transactionService.create(request);

        return ResponseEntity.ok(ResponseDto.success(null,"Transaction created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetTransactionResponse>>> getByFilter(
            @Valid @ModelAttribute TransactionFilter transactionFilter
    ) {
        ResponseDto<List<GetTransactionResponse>> response = transactionService.getByFilter(transactionFilter);

        return ResponseEntity.ok(
                response
        );
    }
}
