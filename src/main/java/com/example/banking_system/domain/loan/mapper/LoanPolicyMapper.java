package com.example.banking_system.domain.loan.mapper;

import com.example.banking_system.domain.loan.dto.CreateLoanPolicyRequest;
import com.example.banking_system.domain.loan.dto.GetLoanPolicyResponse;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanPolicyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LoanPolicy toEntity(CreateLoanPolicyRequest request);

    GetLoanPolicyResponse toDto(LoanPolicy loanPolicy);

    List<GetLoanPolicyResponse> toDtoList(List<LoanPolicy> loanPolicyList);
}

