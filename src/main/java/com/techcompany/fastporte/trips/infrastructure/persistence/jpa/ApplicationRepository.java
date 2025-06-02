package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByDriver_IdAndRequest_Id(Long driverId, Long requestId);

    List<Application> findByRequest_Id(Long requestId);
}
