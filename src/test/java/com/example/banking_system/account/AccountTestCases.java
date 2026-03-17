package com.example.banking_system.account;

import com.example.banking_system.account.constant.Gender;
import com.example.banking_system.account.dto.*;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.entity.PersonalAccount;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountTestCases {
    private  BusinessAccount businessAccountTestCase = new BusinessAccount("username", "password", "email", "phoneNumber", "address", "OrganizationName", "TaxIdNumber");
    private  PersonalAccount personalAccountTestCase = new PersonalAccount("username", "password", "email", "phoneNumber", "address", Gender.FEMALE,"fullName", LocalDate.now(), "idCardNumber");
    private  GovernmentAccount governmentAccountTestCase = new GovernmentAccount("username", "password", "email", "phoneNumber", "address", "governmentDepartment");

    {
        businessAccountTestCase.getAccount().setId(1);
        personalAccountTestCase.getAccount().setId(1);
        governmentAccountTestCase.getAccount().setId(1);
    }

    private static AccountTestCases instance;

    public static AccountTestCases getInstance() {
        if(instance == null) {
            instance = new AccountTestCases();
        }

        return instance;
    }

    public CreateBusinessAccountRequest getCreateBusinessAccountRequestTestCase() {
        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();
        fillCreateAccountRequest(request);
        request.setOrganizationName("OrganizationName");
        request.setTaxIdNumber("TaxIdNumber");
        return request;
    }

    public CreateGovernmentAccountRequest getCreateGovernmentAccountRequestTestCase() {
        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();
        fillCreateAccountRequest(request);
        request.setGovernmentDepartment("governmentDepartment");
        return request;
    }

    public CreatePersonalAccountRequest getCreatePersonalAccountRequestTestCase() {
        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();
        fillCreateAccountRequest(request);
        request.setFullName("fullName");
        request.setDateOfBirth(LocalDate.now());
        request.setIdCardNumber("idCardNumber");
        request.setGender(Gender.UNKNOWN);
        return request;
    }

    private void fillCreateAccountRequest(CreateAccountRequest request) {
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email@gmail.com");
        request.setPhoneNumber("phoneNumber");
        request.setAddress("address");
    }


    public UpdateBusinessAccountRequest getUpdateBusinessAccountRequestTestCase() {
        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        fillUpdateAccountRequest(request);
        request.setOrganizationName("newOrganizationName");
        request.setTaxIdNumber("newTaxIdNumber");
        return request;
    }

    public UpdateGovernmentAccountRequest getUpdateGovernmentAccountRequestTestCase() {
        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();
        fillUpdateAccountRequest(request);
        request.setGovernmentDepartment("newGovernmentDepartment");
        return request;
    }

    public UpdatePersonalAccountRequest getUpdatePersonalAccountRequestTestCase() {
        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();
        fillUpdateAccountRequest(request);
        request.setFullName("newFullName");
        request.setIdCardNumber("newIdCardNumber");
        request.setDateOfBirth(LocalDate.now());
        return request;
    }

    private void fillUpdateAccountRequest(UpdateAccountRequest request) {
        request.setEmail("newEmail@gmail.com");
        request.setPhoneNumber("newPhoneNumber");
        request.setAddress("newAddress");
    }
    
}
