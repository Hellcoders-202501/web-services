package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.PaymentStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.PaymentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    PaymentStatus findByStatus(PaymentStatusType status);
}
