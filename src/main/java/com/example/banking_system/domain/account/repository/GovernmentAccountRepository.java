package com.example.banking_system.domain.account.repository;

import com.example.banking_system.domain.account.entity.GovernmentAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GovernmentAccountRepository extends JpaRepository<GovernmentAccount, Long> {
    List<GovernmentAccount> findByGovernmentDepartment(String governmentDepartment);

    @EntityGraph(attributePaths = {"account"})
    Optional<GovernmentAccount> findByAccount_Username(String username);

    boolean existsByGovernmentDepartment(String governmentDepartment);
}
