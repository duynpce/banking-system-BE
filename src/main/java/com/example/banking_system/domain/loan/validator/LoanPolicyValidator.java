package com.example.banking_system.domain.loan.validator;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.ValidationUtil;
import com.example.banking_system.domain.loan.dto.UpdateLoanPolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LoanPolicyValidator {
    private final LoanPolicyQueryService loanPolicyQueryService;
    private final ValidationUtil validationUtil;

    public void validateCreate(LoanPolicy loanPolicy) {
        validationUtil.validateEffectiveDateRange(loanPolicy.getEffectiveFrom(), loanPolicy.getEffectiveTo());

        if (loanPolicyQueryService.hasOverlap(loanPolicy)) {
            throw new ValidationException("An overlapping loan policy already exists for this loan type");
        }
    }

    public void validateUpdate(UpdateLoanPolicyRequest request, LoanPolicy existingLoanPolicy) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        boolean updateActivePolicy = !existingLoanPolicy.getEffectiveFrom().isAfter(LocalDate.now());

        if(updateActivePolicy) {
            if(request.getEffectiveFrom() == null){
                throw new ValidationException("At least one effective date range must be provided");
            }
        }else{
            if(loanPolicyQueryService.hasOverlap(existingLoanPolicy)){
                throw new ValidationException("Updating this loan policy would cause an overlap with another active policy of the same type");
            }
        }

        validationUtil.validateEffectiveDateRange(existingLoanPolicy.getEffectiveFrom(), existingLoanPolicy.getEffectiveTo());

        setNonNullFieldsToUpdateLoanPolicy(request, existingLoanPolicy);
    }

    private boolean isAllFieldsNull(UpdateLoanPolicyRequest request) {
        return request.getDurationMonths() == null
                && request.getInterestRate() == null
                && request.getLoanType() == null
                && request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null;
    }

    private void setNonNullFieldsToUpdateLoanPolicy(UpdateLoanPolicyRequest request, LoanPolicy existingLoanPolicy) {
        if (request.getDurationMonths() != null) {
            existingLoanPolicy.setDurationMonths(request.getDurationMonths());
        }

        if (request.getInterestRate() != null) {
            existingLoanPolicy.setInterestRate(request.getInterestRate());
        }

        if (request.getLoanType() != null) {
            existingLoanPolicy.setLoanType(request.getLoanType());
        }

        if (request.getEffectiveFrom() != null) {
            existingLoanPolicy.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            existingLoanPolicy.setEffectiveTo(request.getEffectiveTo());
        }
    }

}

