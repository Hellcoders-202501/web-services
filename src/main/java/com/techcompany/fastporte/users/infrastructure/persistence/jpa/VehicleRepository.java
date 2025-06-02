package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByDriver_Id(Long id);
}
