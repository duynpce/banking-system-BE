package com.example.banking_system.domain.transaction;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import com.example.banking_system.domain.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@RequestBody CreateTransactionRequest request) {
        transactionService.create(request);

        return ResponseEntity.ok(ResponseDto.success(null,"Transaction created successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<GetTransactionResponse>>> getByPage(
            @RequestParam Integer page,
            @RequestParam Integer limit
    ) {
        List<GetTransactionResponse> response = transactionService.getByPage(page, limit);
        return ResponseEntity.ok(ResponseDto.success(response, "Transactions retrieved successfully"));
    }

    @GetMapping(params = {"startDate", "endDate"})
    public ResponseEntity<ResponseDto<List<GetTransactionResponse>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<GetTransactionResponse> response = transactionService.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(ResponseDto.success(response, "Transactions retrieved successfully"));
    }
}
