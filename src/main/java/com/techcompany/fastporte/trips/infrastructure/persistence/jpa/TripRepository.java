package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByStatus_Id (Long statusId);
    List<Trip> findAllByRequest_Contract_Driver_IdAndStatus_StatusIsNotAndStatus_StatusIsNotNull(Long driverId, TripStatusType status);

    @Modifying
    @Query("update Trip t set t.status.id = ?2 where t.id = ?1")
    void updateStatusById(Long id, Long statusId);
}