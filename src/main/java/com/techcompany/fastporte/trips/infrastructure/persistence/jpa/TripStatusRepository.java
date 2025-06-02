package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripStatusRepository extends JpaRepository<TripStatus, Long> {

    Optional<TripStatus> findByStatus(TripStatusType tripStatusType);
}
