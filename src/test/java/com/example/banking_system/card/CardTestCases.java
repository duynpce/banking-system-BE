package com.example.banking_system.card;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.dto.*;
import com.example.banking_system.card.entity.CardPrivilegeCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CardTestCases {

    private static CardTestCases instance;

    public static CardTestCases getInstance() {
        if(instance == null) {
            instance = new CardTestCases();
        }

        return instance;
    }

    public CreateBusinessCardRequest getCreateBusinessCardRequestTestCase() {
        CreateBusinessCardRequest request = new CreateBusinessCardRequest();
        fillCreateCardRequest(request);
        request.setCardHolder("John Doe");
        return request;
    }

    public CreatePersonalCardRequest getCreatePersonalCardRequestTestCase() {
        CreatePersonalCardRequest request = new CreatePersonalCardRequest();
        fillCreateCardRequest(request);
        return request;
    }

    private void fillCreateCardRequest(CreateCardRequest request) {
        request.setPrivilegeCode("code");
        request.setPinCode("123456");
        request.setType(CardType.CREDIT);
    }

    public CreateCardPrivilegeRequest getCreateCardPrivilegeRequestTestCase() {
        CreateCardPrivilegeRequest request = new CreateCardPrivilegeRequest();
        request.setCode("code");
        request.setAnnualFee(new BigDecimal("0.1"));
        request.setCashbackRate(new BigDecimal("0.1"));
        request.setCardType(CardType.CREDIT);
        request.setAccountType(AccountType.BUSINESS);
        request.setEffectiveFrom(LocalDate.now());
        request.setEffectiveTo(LocalDate.now().plusYears(1));
        request.setActive(true);
        return request;
    }

    public UpdateCardPrivilegeRequest getUpdateCardPrivilegeRequestTestCase() {
        UpdateCardPrivilegeRequest request = new UpdateCardPrivilegeRequest();
        request.setCode("code");
        request.setAnnualFee(new BigDecimal("0.2"));
        request.setCashBackRate(new BigDecimal("0.2"));
        request.setEffectiveFrom(LocalDate.now());
        request.setEffectiveTo(LocalDate.now().plusYears(2));
        return request;
    }

    public CardPrivilegeCode getCardPrivilegeCodeTestCase() {
        CardPrivilegeCode code = new CardPrivilegeCode();
        code.setCode("code");
        code.setExpirationYears(5);
        code.setSpendingLimitDaily(new BigDecimal("1000.00"));
//        code.setEffectiveFrom(LocalDate.now());
//        code.setEffectiveTo(LocalDate.now().plusYears(1));
        return code;
    }

}
