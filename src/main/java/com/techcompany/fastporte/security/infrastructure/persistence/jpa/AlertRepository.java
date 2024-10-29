package com.techcompany.fastporte.security.infrastructure.persistence.jpa;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findBySensorData_TripId(Long tripId);
}
