package com.example.banking_system.domain.loan.mapper;

import com.example.banking_system.domain.loan.dto.CreateLoanFineRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFineResponse;
import com.example.banking_system.domain.loan.entity.LoanFine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanFineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loan", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LoanFine toEntity(CreateLoanFineRequest request);

    @Mapping(target = "loanId", source = "loan.id")
    GetLoanFineResponse toDto(LoanFine loanFine);

    List<GetLoanFineResponse> toDtoList(List<LoanFine> loanFineList);
}

