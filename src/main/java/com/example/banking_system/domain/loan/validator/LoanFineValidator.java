package com.example.banking_system.domain.loan.validator;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.loan.dto.UpdateLoanFineRequest;
import com.example.banking_system.domain.loan.entity.LoanFine;
import org.springframework.stereotype.Component;

@Component
public class LoanFineValidator {

    public void validateCreate(LoanFine loanFine) {
        if (loanFine.getAmount().signum() <= 0) {
            throw new ValidationException("amount must be greater than 0");
        }
    }

    public void validateUpdate(UpdateLoanFineRequest request, LoanFine existingLoanFine) {
        if (request.getAmount() == null && request.getType() == null) {
            throw new ValidationException("At least one field must be provided for update");
        }

        if (request.getAmount() != null) {
            existingLoanFine.setAmount(request.getAmount());
        }

        if (request.getType() != null) {
            existingLoanFine.setType(request.getType());
        }
    }
}

