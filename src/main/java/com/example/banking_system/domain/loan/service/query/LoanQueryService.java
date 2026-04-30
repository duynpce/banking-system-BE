package com.example.banking_system.domain.loan.service.query;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.GetLoanReportProjection;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.dto.LoanFilter;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.repository.LoanRepository;
import com.example.banking_system.domain.loan.specification.LoanSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    public GetLoanReportResponse findReportByAccountIdAndStatus(long accountId, LoanStatus loanStatus) {
        GetLoanReportProjection projection = loanRepository.findReportByAccountIdAndStatus(accountId, loanStatus.name());
        GetLoanReportResponse response = new GetLoanReportResponse();
        response.setLoanStatus(loanStatus);
        response.setTotalAmount(projection.getTotalAmount());
        response.setLeftAmount(projection.getLeftAmount());
        response.setMonthlyInstallment(projection.getMonthlyInstallment());
        return response;
    }

    public Page<Loan> findByFilter(long accountId, LoanFilter loanFilter) {
        PageRequest pageable = PageRequest.of(
                loanFilter.getPaginationDto().getPage(),
                loanFilter.getPaginationDto().getLimit()
        );

        Specification<Loan> specification = LoanSpecification.hasAccountId(accountId)
                .and(LoanSpecification.hasStatus(loanFilter.getStatus()))
                .and(LoanSpecification.hasType(loanFilter.getLoanType()));

        if (loanFilter.getStartDate() != null && loanFilter.getEndDate() != null) {
            specification = specification.and(
                    LoanSpecification.createdAtBetween(loanFilter.getStartDate(), loanFilter.getEndDate())
            );
        }

        return loanRepository.findAll(specification, pageable);
    }
}
