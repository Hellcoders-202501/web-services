package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByDriver_id(Long driverId);
}
