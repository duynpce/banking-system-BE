package com.example.banking_system.account.repository;

import com.example.banking_system.account.entity.GovernmentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentAccountRepository extends JpaRepository<GovernmentAccount, Long> {
    List<GovernmentAccount> findByGovernmentDepartment(String governmentDepartment);

    boolean existsByGovernmentDepartment(String governmentDepartment);
}
