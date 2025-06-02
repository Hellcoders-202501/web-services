package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
