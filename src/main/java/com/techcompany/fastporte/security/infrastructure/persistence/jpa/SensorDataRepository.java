package com.techcompany.fastporte.security.infrastructure.persistence.jpa;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    Optional<SensorData> findByTripIdAndSensorType_Id(Long tripId, Long sensorTypeId);
}
