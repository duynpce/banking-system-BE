package com.example.banking_system.account;

import com.example.banking_system.dto.account.*;
import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
public class TestCases {
    private final BusinessAccount businessAccountTestCase = new BusinessAccount("username", "password", "email", "phoneNumber", "address", "OrganizationName", "TaxIdNumber");
    private final PersonalAccount personalAccountTestCase = new PersonalAccount("username", "password", "email", "phoneNumber", "address", "fullName", LocalDate.now(), "idCardNumber");
    private final GovernmentAccount governmentAccountTestCase = new GovernmentAccount("username", "password", "email", "phoneNumber", "address", "governmentDepartment");


    private static TestCases instance;

    public static TestCases getInstance() {
        if(instance == null) {
            instance = new TestCases();
        }

        return instance;
    }

    public CreateBusinessAccountRequest getCreateBusinessAccountRequestTestCase() {
        CreateBusinessAccountRequest request = new CreateBusinessAccountRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");
        request.setPhoneNumber("phoneNumber");
        request.setAddress("address");;
        request.setOrganizationName("OrganizationName");
        request.setTaxIdNumber("TaxIdNumber");
        return request;
    }

    public CreateGovernmentAccountRequest getCreateGovernmentAccountRequestTestCase() {
        CreateGovernmentAccountRequest request = new CreateGovernmentAccountRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");
        request.setPhoneNumber("phoneNumber");
        request.setAddress("address");;
        request.setGovernmentDepartment("governmentDepartment");
        return request;
    }

    public CreatePersonalAccountRequest getCreatePersonalAccountRequestTestCase() {
        CreatePersonalAccountRequest request = new CreatePersonalAccountRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setEmail("email");
        request.setPhoneNumber("phoneNumber");
        request.setAddress("address");;
        request.setFullName("fullName");
        request.setDateOfBirth(LocalDate.now());
        request.setIdCardNumber("idCardNumber");
        return request;
    }

    public UpdateBusinessAccountRequest getUpdateBusinessAccountRequestTestCase() {
        UpdateBusinessAccountRequest request = new UpdateBusinessAccountRequest();
        request.setEmail("newEmail");
        request.setPhoneNumber("newPhoneNumber");
        request.setAddress("newAddress");
        request.setOrganizationName("newOrganizationName");
        request.setTaxIdNumber("newTaxIdNumber");
        return request;
    }

    public UpdateGovernmentAccountRequest getUpdateGovernmentAccountRequestTestCase() {
        UpdateGovernmentAccountRequest request = new UpdateGovernmentAccountRequest();
        request.setEmail("newEmail");
        request.setPhoneNumber("newPhoneNumber");
        request.setAddress("newAddress");
        request.setGovernmentDepartment("newGovernmentDepartment");
        return request;
    }

    public UpdatePersonalAccountRequest getUpdatePersonalAccountRequestTestCase() {
        UpdatePersonalAccountRequest request = new UpdatePersonalAccountRequest();
        request.setEmail("newEmail");
        request.setPhoneNumber("newPhoneNumber");
        request.setAddress("newAddress");
        request.setFullName("newFullName");
        request.setIdCardNumber("newIdCardNumber");
        request.setDateOfBirth(LocalDate.now());
        return request;
    }
    
}
