package com.example.banking_system.domain.loan.service.domain;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.loan.dto.CreateLoanFineRequest;
import com.example.banking_system.domain.loan.dto.GetLoanFineResponse;
import com.example.banking_system.domain.loan.dto.UpdateLoanFineRequest;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanFine;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.mapper.LoanFineMapper;
import com.example.banking_system.domain.loan.service.query.LoanFinePolicyQueryService;
import com.example.banking_system.domain.loan.service.query.LoanFineQueryService;
import com.example.banking_system.domain.loan.service.query.LoanQueryService;
import com.example.banking_system.domain.loan.validator.LoanFineValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanFineService {
	private final AccountQueryService accountQueryService;
	private final LoanFineQueryService loanFineQueryService;
	private final LoanQueryService loanQueryService;
	private final LoanFineMapper loanFineMapper;
	private final LoanFineValidator loanFineValidator;
	private final LoanFinePolicyQueryService loanFinePolicyQueryService;
	private final JwtUtil jwtUtil;

	//admin or monthly execute
	@Transactional
	public LoanFine create(CreateLoanFineRequest request) {
        Account account = accountQueryService.findById(request.getAccountId());
		LoanFine loanFine = loanFineMapper.toEntity(request);
		Loan loan = loanQueryService.findById(request.getLoanId());
		LoanFinePolicy loanFinePolicy = loanFinePolicyQueryService.findById(request.getLoanFinePolicyId());
		loanFine.setLoan(loan);
		loanFine.setAccount(account);
		loanFine.setLoanFinePolicy(loanFinePolicy);

		loanFineValidator.validateCreate(loanFine);
		return loanFineQueryService.save(loanFine);
	}

	//admin
	@Transactional
	public LoanFine update(UpdateLoanFineRequest request) {
		LoanFine loanFine = loanFineQueryService.findById(request.getId());
		loanFineValidator.validateUpdate(request, loanFine);
		return loanFineQueryService.save(loanFine);
	}

	@Transactional(readOnly = true)
	public GetLoanFineResponse getById(long id) {
		LoanFine loanFine = loanFineQueryService.findById(id);
		return loanFineMapper.toDto(loanFine);
	}

	@Transactional(readOnly = true)
	public List<GetLoanFineResponse> getByPage(PaginationDto paginationDto) {
		long accountId = jwtUtil.getJwtClaims().getClaim("account_id");

		Page<LoanFine> loanFinePage = loanFineQueryService.findByAccountIdWithPagination(accountId,paginationDto);
		return loanFineMapper.toDtoList(loanFinePage.getContent());
	}
}
