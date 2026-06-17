package com.example.GasTuanDat.account;

import com.example.GasTuanDat.account.entity.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByUsername(String username);

    boolean existsByEmployee_EmployeeId(UUID employeeId);
}
