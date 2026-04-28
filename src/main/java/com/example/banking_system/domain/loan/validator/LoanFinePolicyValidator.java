package com.example.banking_system.domain.loan.validator;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.ValidationUtil;
import com.example.banking_system.domain.loan.dto.UpdateLoanFinePolicyRequest;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.service.query.LoanFinePolicyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LoanFinePolicyValidator {
    private final LoanFinePolicyQueryService LoanFinePolicyQueryService;
    private final ValidationUtil validationUtil;

    public void validateCreate(LoanFinePolicy LoanFinePolicy) {
        validationUtil.validateEffectiveDateRange(LoanFinePolicy.getEffectiveFrom(), LoanFinePolicy.getEffectiveTo());

        if (LoanFinePolicyQueryService.hasOverlap(LoanFinePolicy)) {
            throw new ValidationException("An overlapping loan policy already exists for this loan type");
        }
    }

    public void validateUpdate(UpdateLoanFinePolicyRequest request, LoanFinePolicy existingLoanFinePolicy) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        boolean updateActivePolicy = !existingLoanFinePolicy.getEffectiveFrom().isAfter(LocalDate.now());

        if (updateActivePolicy) {
            if (request.getEffectiveFrom() == null) {
                throw new ValidationException("At least one effective date range must be provided");
            }
        } else {
            if (LoanFinePolicyQueryService.hasOverlap(existingLoanFinePolicy)) {
                throw new ValidationException("Updating this loan policy would cause an overlap with another active policy of the same type");
            }
        }

        validationUtil.validateEffectiveDateRange(existingLoanFinePolicy.getEffectiveFrom(), existingLoanFinePolicy.getEffectiveTo());

        setNonNullFieldsToUpdateLoanFinePolicy(request, existingLoanFinePolicy);
    }

    private boolean isAllFieldsNull(UpdateLoanFinePolicyRequest request) {
        return request.getAmount() == null
                && request.getLoanFineType() == null
                && request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null;
    }

    private void setNonNullFieldsToUpdateLoanFinePolicy(UpdateLoanFinePolicyRequest request, LoanFinePolicy existingLoanFinePolicy) {

        if (request.getLoanFineType() != null) {
            existingLoanFinePolicy.setLoanFineType(request.getLoanFineType());
        }

        if (request.getAmount() != null) {
            existingLoanFinePolicy.setAmount(request.getAmount());
        }

        if (request.getEffectiveFrom() != null) {
            existingLoanFinePolicy.setEffectiveFrom(request.getEffectiveFrom());
        }

        if (request.getEffectiveTo() != null) {
            existingLoanFinePolicy.setEffectiveTo(request.getEffectiveTo());
        }
    }
}