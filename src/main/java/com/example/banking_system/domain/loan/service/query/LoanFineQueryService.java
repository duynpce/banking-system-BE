package com.example.banking_system.domain.loan.service.query;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanFine;
import com.example.banking_system.domain.loan.repository.LoanFineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanFineQueryService {
    private final LoanFineRepository loanFineRepository;

    public LoanFine save(LoanFine loanFine) {
        return loanFineRepository.save(loanFine);
    }

    public LoanFine findById(long id) {
        return loanFineRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Loan fine not found with id: " + id)
        );
    }

    public Page<LoanFine> findByAccountIdWithPagination(long accountId, PaginationDto paginationDto) {
        return loanFineRepository.findByAccountId(accountId, PageRequest.of(paginationDto.getPage(), paginationDto.getLimit()));
    }

}
