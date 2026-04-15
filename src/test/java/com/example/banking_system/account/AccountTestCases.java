package com.example.banking_system.account;

import com.example.banking_system.domain.account.constant.Gender;
import com.example.banking_system.domain.account.dto.*;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.entity.BusinessAccount;
import com.example.banking_system.domain.account.entity.GovernmentAccount;
import com.example.banking_system.domain.account.entity.PersonalAccount;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountTestCases {
    private  BusinessAccount businessAccountTestCase = new BusinessAccount("username1", "Password123@", "email1@gmail.com", "0123456789", "address", "OrganizationName", "0123456789");
    private  PersonalAccount personalAccountTestCase = new PersonalAccount("username2", "Password123@", "email2@gmail.com", "1234567890", "address", Gender.FEMALE,"fullName", LocalDate.now().minusYears(20) , "0123456789");
    private  GovernmentAccount governmentAccountTestCase = new GovernmentAccount("username3", "Password123@", "email3@gmail.com", "2345678901", "address", "governmentDepartment");

    {
        businessAccountTestCase.getAccount().setId(1L);
        personalAccountTestCase.getAccount().setId(2L);
        governmentAccountTestCase.getAccount().setId(3L);

        businessAccountTestCase.getAccount().setNumber("012345678901");
        personalAccountTestCase.getAccount().setNumber("012345678902");
        governmentAccountTestCase.getAccount().setNumber("012345678903");
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
        request.setUsername("username2");
        request.setPassword("password");
        request.setEmail("email2@gmail.com");
        request.setPhoneNumber("2123456789");
        request.setAddress("address");
        request.setOrganizationName("OrganizationName");
        request.setTaxIdNumber("0123456789");
        return request;
    }

    public CreateGovernmentAccountRequest getCreateGovernmentAccountRequestTestCase() {
        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();
        request.setUsername("username3");
        request.setPassword("password");
        request.setEmail("email3@gmail.com");
        request.setPhoneNumber("3123456789");
        request.setAddress("address");
        request.setGovernmentDepartment("governmentDepartment");
        return request;
    }

    public CreatePersonalAccountRequest getCreatePersonalAccountRequestTestCase() {
        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();
        request.setUsername("username1");
        request.setPassword("password");
        request.setEmail("email1@gmail.com");
        request.setPhoneNumber("1123456789");
        request.setAddress("address");
        request.setFullName("fullName");
        request.setDateOfBirth(LocalDate.now().minusYears(20));
        request.setIdCardNumber("0123456789");
        request.setGender(Gender.UNKNOWN);
        return request;
    }

    public UpdateBusinessAccountRequest getUpdateBusinessAccountRequestTestCase() {
        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        fillUpdateAccountRequest(request);
        request.setOrganizationName("newOrganizationName");
        request.setTaxIdNumber("01234567890");
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
        request.setIdCardNumber("0987654321");
        request.setDateOfBirth(LocalDate.now().minusYears(21));
        return request;
    }

    private void fillUpdateAccountRequest(UpdateAccountRequest request) {
        request.setEmail("newEmail@gmail.com");
        request.setPhoneNumber("0987654321");
        request.setAddress("newAddress");
    }
    
}
