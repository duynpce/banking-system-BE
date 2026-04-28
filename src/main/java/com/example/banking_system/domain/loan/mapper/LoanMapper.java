package com.example.banking_system.domain.loan.mapper;

import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = LoanStatus.class)
public interface LoanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(LoanStatus.CURRENT_PAYMENT)")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "policy", ignore = true)
    @Mapping(target = "totalAmount" , source = "request.amount")
    Loan toEntity(CreateLoanRequest request);

    GetLoanResponse toDto(Loan loan);

    List<GetLoanResponse> toDtoList(List<Loan> loanList);
}

