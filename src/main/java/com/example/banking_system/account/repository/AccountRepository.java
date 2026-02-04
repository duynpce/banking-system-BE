package com.example.banking_system.account.repository;

import com.example.banking_system.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banking_system.account.entity.BusinessAccount;
import com.example.banking_system.account.entity.GovernmentAccount;
import com.example.banking_system.account.entity.PersonalAccount;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByPhoneNumber(String phoneNumber);
    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

//    @Query("""
//           select a
//           from Account a
//           left join fetch a.accountDetails d
//           left join fetch treat(d as BusinessAccount)
//           left join fetch treat(d as GovernmentAccount)
//           left join fetch treat(d as PersonalAccount)
//           where a.username = :username
//           """)
//    Optional<Account> findByUsernameWithDetails(String username);
}
