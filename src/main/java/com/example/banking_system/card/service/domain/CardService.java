package com.example.banking_system.card.service.domain;

import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.CardDetails;
import com.example.banking_system.card.mapper.CardMapper;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.service.query.CardQueryService;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.pricingmultiplier.PricingMultiplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AccountQueryService accountQueryService;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final PricingMultiplierService pricingMultiplierService;
    private final JwtUtil jwtUtil;
    private final CardQueryService cardQueryService;
    private final CardMapper cardMapper;

    @Value("${value.bin}")
    private String BIN;

    public void updateExpirationDateOnCreate(Card card) {
        LocalDate expirationDate = getExpirationDate(card);
        card.setExpirationDate(expirationDate);

    }

    public LocalDate getExpirationDate(Card card) {
        int baseExpirationYears = cardPrivilegeQueryService.findByCode(card.getPrivilege().getPrivilegeCode()).getExpirationYears();

        return LocalDate.now().plusYears(baseExpirationYears);
    }

    public void extendExpirationDate(Card card, int yearsToExtend) {
        card.setExpirationDate(card.getExpirationDate().plusYears(yearsToExtend));
    }


    @Transactional
    public List<? extends GetCardResponse> GetAllCardByJwt() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        List<CardDetails> cardDetailsList = account.getCardDetailsList();
        return cardMapper.toDtoList(cardDetailsList);
    }


    @Transactional
    public GetCardResponse getCardById(long id) {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        Card card = cardQueryService.findById(id);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to access this card");
        }


        return cardMapper.toDto(card.getCardDetails());
    }

//    public BigDecimal getAnnualFee(Card card) {
//        String multiplierKind = "ANNUAL_FEE";
//        BigDecimal pricingMultiplier = pricingMultiplierService
//                .findByAccountTypeAndMultiplierKind(card.getAccount().getType(), multiplierKind)
//                .getMultiplierValue();
//        BigDecimal annualFee = cardPrivilegeQueryService.findByCode(card.getPrivilege().getPrivilegeCode()).getAnnualFee();
//
//        return annualFee.multiply(pricingMultiplier);
//    }
//
//    public BigDecimal getCashbackRateById(long id) {
//        final String username = jwtUtil.getUsername();
//        Account account = accountQueryService.findByUsername(username);
//        Card card = cardQueryService.findById(id);
//
//        if(card.getAccount().getId() != account.getId()) {
//            throw new ForbiddenException("You are not allowed to access this card");
//        }
//
//        return getCashBackRate(card);
//    }
//
//    public BigDecimal getCashBackRate(Card card) {
//        String multiplierKind = "CASHBACK_RATE_" + card.getType().name();
//        BigDecimal pricingMultiplier = pricingMultiplierService
//                .findByAccountTypeAndMultiplierKind(card.getAccount().getType(), multiplierKind)
//                .getMultiplierValue();
//        BigDecimal baseCashbackRate = cardPrivilegeQueryService.findByCode(card.getPrivilege().getPrivilegeCode()).getCashbackRate();
//
//        return baseCashbackRate.multiply(pricingMultiplier);
//    }

    public void deleteCardById(long id) {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        Card card = cardQueryService.findById(id);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to delete this card");
        }

        cardQueryService.delete(card);
    }


    public String generateCardNumber() {
        String sequence = String.valueOf(cardQueryService.getCardNumberSequence()).formatted("%012d");
        System.out.println(BIN + " " + sequence);
        return BIN + sequence;
    }

}
