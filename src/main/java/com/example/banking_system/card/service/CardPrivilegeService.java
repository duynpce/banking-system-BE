package com.example.banking_system.card.service;

import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.card.repository.CardPrivilegeRepository;
import com.example.banking_system.card.validator.CardPrivilegeValidator;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.pricingmultiplier.PricingMultiplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardPrivilegeService {
    private final CardPrivilegeRepository cardPrivilegeRepository;
    private final PricingMultiplierService pricingMultiplierService;
    private final CardPrivilegeValidator cardPrivilegeValidator;
    private final CardPrivilegeMapper CardPrivilegeMapper;

    public CardPrivilege create(CreateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = CardPrivilegeMapper.toEntity(request);
        cardPrivilegeValidator.validateCreate(cardPrivilege);

        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public CardPrivilege update(UpdateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = findByPrivilegeCode(request.getCode());
        cardPrivilegeValidator.validateUpdate(request, cardPrivilege);
        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public BigDecimal getAnnualFee(Card card) {
        String multiplierKind = "ANNUAL_FEE";
        BigDecimal pricingMultiplier = pricingMultiplierService
                .findByAccountTypeAndMultiplierKind(card.getHolderType(), multiplierKind)
                .getMultiplierValue();
        BigDecimal annualFee = findByPrivilegeCode(card.getPrivilege().getCode()).getBaseAnnualFee();

        return annualFee.multiply(pricingMultiplier);
    }

    public BigDecimal getCashBackRate(Card card) {
        String multiplierKind = "CASHBACK_RATE_" + card.getType().name();
        BigDecimal pricingMultiplier = pricingMultiplierService
                .findByAccountTypeAndMultiplierKind(card.getHolderType(), multiplierKind)
                .getMultiplierValue();
        BigDecimal baseCashbackRate = findByPrivilegeCode(card.getPrivilege().getCode()).getBaseCashbackRate();

        return baseCashbackRate.multiply(pricingMultiplier);
    }

    public LocalDate getExpirationDate(Card card) {
        int baseExpirationYears = findByPrivilegeCode(card.getPrivilege().getCode()).getBaseExpirationYears();

        return LocalDate.now().plusYears(baseExpirationYears);
    }

    public void deleteCardPrivilegeByCode(String code) {
        CardPrivilege cardPrivilege = findByPrivilegeCode(code);

        cardPrivilegeRepository.delete(cardPrivilege);
    }

    public CardPrivilege findByPrivilegeCode(String privilegeCode) {
        return cardPrivilegeRepository.findById(privilegeCode).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public boolean existsByCode(String code) {
        return cardPrivilegeRepository.existsById(code);
    }

}
