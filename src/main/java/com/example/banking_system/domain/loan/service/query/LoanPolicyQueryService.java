package com.example.banking_system.domain.loan.service.query;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.repository.LoanPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanPolicyQueryService {
    private final LoanPolicyRepository loanPolicyRepository;

    public LoanPolicy save(LoanPolicy loanPolicy) {
        return loanPolicyRepository.save(loanPolicy);
    }

    public LoanPolicy findById(long id) {
        return loanPolicyRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Loan policy not found with id: " + id)
        );
    }

    public Page<LoanPolicy> findAllWithPagination(PaginationDto paginationDto) {
        return loanPolicyRepository.findAll(PageRequest.of(paginationDto.getPage(), paginationDto.getLimit()));
    }

    public List<LoanPolicy> findByLoanTypeAndIsActive(LoanType loanType) {
        return loanPolicyRepository.findByLoanTypeAndDate(loanType, LocalDate.now());
    }

    public List<LoanPolicy> findByLoanTypeAndDate(LoanType loanType, LocalDate date) {
        return loanPolicyRepository.findByLoanTypeAndDate(loanType, date);
    }

    public boolean hasOverlap(LoanPolicy loanPolicy) {
        return loanPolicyRepository.hasOverlap(loanPolicy.getLoanType(), loanPolicy.getEffectiveFrom(), loanPolicy.getEffectiveTo());
    }

}
