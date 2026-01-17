package com.example.banking_system.account;


import com.example.banking_system.entity.account.BusinessAccount;
import com.example.banking_system.entity.account.GovernmentAccount;
import com.example.banking_system.entity.account.PersonalAccount;
import lombok.Getter;

import java.time.LocalDate;

public class TestCases {
    // please create "testCase" variables for PersonalAccount and GovernmentAccount similar to the BusinessAccount example below
    private final PersonalAccount personalAccount = new PersonalAccount("username", "password", "email", "phoneNumber", "address", "fullName", LocalDate.now(), "idCardNumber");
    private final GovernmentAccount governmentAccount = new GovernmentAccount("username", "password", "email", "phoneNumber", "address","governmentDepartment");

}
