package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountTypeRepository extends JpaRepository<BankAccountType, Long> {
}
