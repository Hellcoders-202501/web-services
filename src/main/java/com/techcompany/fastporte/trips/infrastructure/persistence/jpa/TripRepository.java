package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByDriverId(Long id);
    List<Trip> findAllByDriverIdAndStatus_Id(Long driverId, Long statusId);

    List<Trip> findAllBySupervisorId(Long id);
    List<Trip> findAllBySupervisorIdAndStatus_Id(Long supervisorId, Long statusId);

    List<Trip> findAllByDriverIdIn(List<Long> driverIds);
    List<Trip> findAllByStatus_Id (Long statusId);
}