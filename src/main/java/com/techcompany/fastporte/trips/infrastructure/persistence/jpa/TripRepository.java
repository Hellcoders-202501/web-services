package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

//    List<Trip> findAllByDriverId(Long id);
//    List<Trip> findAllByDriverIdAndStatus_Id(Long driverId, Long statusId);
//
//    List<Trip> findAllByClientId(Long id);
//    List<Trip> findAllByClientIdAndStatus_Id(Long clientId, Long statusId);
//
//    List<Trip> findAllByDriverIdIn(List<Long> driverIds);
    List<Trip> findAllByStatus_Id (Long statusId);

    @Modifying
    @Query("update Trip t set t.status.id = ?2 where t.id = ?1")
    void updateStatusById(Long id, Long statusId);
}