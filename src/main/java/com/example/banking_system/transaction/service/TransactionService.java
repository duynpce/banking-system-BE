package com.example.banking_system.transaction.service;

import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.transaction.TransactionMapper;
import com.example.banking_system.transaction.dto.GetTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public List<GetTransactionResponse> getByDateRange(LocalDate startDate, LocalDate endDate) {
        String username = jwtUtil.getUsername();

        return transactionMapper.toDtoList(
                transactionQueryService.findByFromAccountAndDateRange(username, startDate, endDate)
        );
    }

    @Transactional(readOnly = true)
    public List<GetTransactionResponse> getByPage(Integer page, Integer limit) {
        String username = jwtUtil.getUsername();

        return transactionMapper.toDtoList(
                transactionQueryService.findByFromAccount(username, page, limit).getContent()
        );
    }
}
