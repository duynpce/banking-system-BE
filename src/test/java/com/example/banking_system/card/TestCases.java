package com.example.banking_system.card;

import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import lombok.Getter;

@Getter
public class TestCases {

    private static TestCases instance;

    public static TestCases getInstance() {
        if(instance == null) {
            instance = new TestCases();
        }

        return instance;
    }

    public CreateBusinessCardRequest getCreateBusinessCardRequestTestCase() {
        CreateBusinessCardRequest request = new CreateBusinessCardRequest();
        fillCreateCardRequest(request);
        request.setAuthorizedPersonName("authorizedPersonName");
        return request;
    }

    public CreatePersonalCardRequest getCreatePersonalCardRequestTestCase() {
        CreatePersonalCardRequest request = new CreatePersonalCardRequest();
        fillCreateCardRequest(request);
        return request;
    }

    private void fillCreateCardRequest(com.example.banking_system.card.dto.CreateCardRequest request) {
        request.setPrivilegeCode("privilegeCode");
        request.setType(CardType.CREDIT);
    }
}
