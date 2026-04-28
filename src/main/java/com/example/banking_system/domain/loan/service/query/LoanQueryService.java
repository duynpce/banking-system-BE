package com.example.banking_system.domain.loan.service.query;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanQueryService {
    private final LoanRepository loanRepository;

    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan findById(long id) {
        return loanRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Loan not found with id: " + id)
        );
    }

    public Loan findByIdAndAccountId(long loanId, long accountId) {
        return loanRepository.findByIdAndAccountId(loanId, accountId).orElseThrow(
                () -> new NotFoundException("Loan not found with id: " + loanId + " and account id: " + accountId)
        );
    }


    public Page<Loan> findByAccountIdWithPagination(long accountId, PaginationDto paginationDto) {
        return loanRepository.findByAccountId(accountId, PageRequest.of(paginationDto.getPage(), paginationDto.getLimit()));
    }
}
