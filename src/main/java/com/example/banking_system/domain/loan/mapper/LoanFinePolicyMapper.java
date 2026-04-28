package com.example.banking_system.domain.loan.mapper;

import com.example.banking_system.domain.loan.dto.CreateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFinePolicyResponse;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanFinePolicyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LoanFinePolicy toEntity(CreateLoanFinePolicyRequest request);

    GetLoanFinePolicyResponse toDto(LoanFinePolicy loanFinePolicy);

    List<GetLoanFinePolicyResponse> toDtoList(List<LoanFinePolicy> loanFinePolicyList);
}

