package com.techcompany.fastporte.security.infrastructure.persistence.jpa;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorType;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorTypeRepository extends JpaRepository<SensorType, Long> {
    Optional<SensorType> findByType(Type name);
}
