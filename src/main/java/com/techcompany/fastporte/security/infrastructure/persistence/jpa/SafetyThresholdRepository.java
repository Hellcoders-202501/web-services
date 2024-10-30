package com.techcompany.fastporte.security.infrastructure.persistence.jpa;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafetyThresholdRepository extends JpaRepository<SafetyThreshold, Long> {
    List<SafetyThreshold> findByTripId(Long tripId);
}
