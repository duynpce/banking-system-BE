package com.example.banking_system.domain.loan.service.query;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.repository.LoanFinePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanFinePolicyQueryService {
    private final LoanFinePolicyRepository loanFinePolicyRepository;

    public LoanFinePolicy save(LoanFinePolicy loanFinePolicy) {
        return loanFinePolicyRepository.save(loanFinePolicy);
    }

    public LoanFinePolicy findById(long id) {
        return loanFinePolicyRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Loan fine policy not found with id: " + id)
        );
    }

    public Page<LoanFinePolicy> findAllWithPagination(PaginationDto paginationDto) {
        return loanFinePolicyRepository.findAll(PageRequest.of(paginationDto.getPage(), paginationDto.getLimit()));
    }

    public List<LoanFinePolicy> findByLoanFineTypeAndIsActive(LoanFineType loanFineType) {
        return loanFinePolicyRepository.findByLoanFineTypeAndDate(loanFineType,  LocalDate.now());
    }



    public boolean hasOverlap(LoanFinePolicy loanFinePolicy) {
        return loanFinePolicyRepository.hasOverlap(loanFinePolicy.getLoanFineType(), loanFinePolicy.getEffectiveFrom(), loanFinePolicy.getEffectiveTo());
    }

}

