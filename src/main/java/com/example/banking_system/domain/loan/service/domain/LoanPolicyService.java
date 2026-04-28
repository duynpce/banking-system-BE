package com.example.banking_system.domain.loan.service.domain;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanPolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.mapper.LoanPolicyMapper;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
import com.example.banking_system.domain.loan.validator.LoanPolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanPolicyService {
    private final LoanPolicyQueryService loanPolicyQueryService;
    private final LoanPolicyMapper loanPolicyMapper;
    private final LoanPolicyValidator loanPolicyValidator;

    @Transactional
    public LoanPolicy create(CreateLoanPolicyRequest request) {
        LoanPolicy loanPolicy = loanPolicyMapper.toEntity(request);
        loanPolicyValidator.validateCreate(loanPolicy);
        return loanPolicyQueryService.save(loanPolicy);
    }

    @Transactional
    public LoanPolicy update(UpdateLoanPolicyRequest request) {
        LoanPolicy loanPolicy = loanPolicyQueryService.findById(request.getId());
        loanPolicyValidator.validateUpdate(request, loanPolicy);
        return loanPolicyQueryService.save(loanPolicy);
    }

    @Transactional(readOnly = true)
    public GetLoanPolicyResponse getById(long id) {
        LoanPolicy loanPolicy = loanPolicyQueryService.findById(id);
        return loanPolicyMapper.toDto(loanPolicy);
    }

    @Transactional(readOnly = true)
    public List<GetLoanPolicyResponse> getByPage(PaginationDto paginationDto) {
        Page<LoanPolicy> loanPolicyPage = loanPolicyQueryService.findAllWithPagination(paginationDto);
        return loanPolicyMapper.toDtoList(loanPolicyPage.getContent());
    }

    @Transactional(readOnly = true)
    public List<GetLoanPolicyResponse> getByLoanTypeAndIsActive(LoanType loanType) {
        List<LoanPolicy> loanPolicyPage = loanPolicyQueryService.findByLoanTypeAndIsActive(loanType);
        return loanPolicyMapper.toDtoList(loanPolicyPage);
    }
}
