package com.example.banking_system.domain.loan.service.domain;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.dto.CreateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFinePolicyResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.mapper.LoanFinePolicyMapper;
import com.example.banking_system.domain.loan.service.query.LoanFinePolicyQueryService;
import com.example.banking_system.domain.loan.validator.LoanFinePolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanFinePolicyService {
    private final LoanFinePolicyQueryService LoanFinePolicyQueryService;
    private final LoanFinePolicyMapper LoanFinePolicyMapper;
    private final LoanFinePolicyValidator LoanFinePolicyValidator;

    @Transactional
    public LoanFinePolicy create(CreateLoanFinePolicyRequest request) {
        LoanFinePolicy LoanFinePolicy = LoanFinePolicyMapper.toEntity(request);
        LoanFinePolicyValidator.validateCreate(LoanFinePolicy);
        return LoanFinePolicyQueryService.save(LoanFinePolicy);
    }

    @Transactional
    public LoanFinePolicy update(UpdateLoanFinePolicyRequest request) {
        LoanFinePolicy LoanFinePolicy = LoanFinePolicyQueryService.findById(request.getId());
        LoanFinePolicyValidator.validateUpdate(request, LoanFinePolicy);
        return LoanFinePolicyQueryService.save(LoanFinePolicy);
    }

    @Transactional(readOnly = true)
    public GetLoanFinePolicyResponse getById(long id) {
        LoanFinePolicy LoanFinePolicy = LoanFinePolicyQueryService.findById(id);
        return LoanFinePolicyMapper.toDto(LoanFinePolicy);
    }

    @Transactional(readOnly = true)
    public List<GetLoanFinePolicyResponse> getByPage(PaginationDto paginationDto) {
        Page<LoanFinePolicy> LoanFinePolicyPage = LoanFinePolicyQueryService.findAllWithPagination(paginationDto);
        return LoanFinePolicyMapper.toDtoList(LoanFinePolicyPage.getContent());
    }

    @Transactional(readOnly = true)
    public List<GetLoanFinePolicyResponse> getByLoanFineTypeAndIsActive(LoanFineType loanFineType) {
        List<LoanFinePolicy> LoanFinePolicies = LoanFinePolicyQueryService.findByLoanFineTypeAndIsActive(loanFineType);
        return LoanFinePolicyMapper.toDtoList(LoanFinePolicies);
    }
}
