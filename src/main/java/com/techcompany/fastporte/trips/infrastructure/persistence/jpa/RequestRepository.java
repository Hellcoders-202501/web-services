package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.RequestStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByService_IdAndStatus_StatusIsNot(Long serviceId, RequestStatusType status);
    List<Request> findAllByClient_IdAndStatus_StatusIsNot(Long serviceId, RequestStatusType status);

    Optional<Request> findByTrip_Id(Long tripId);
    List<Request> findAllByStatus_Status(RequestStatusType status);
    List<Request> findAllByClient_Id(Long clientId);
    List<Request> findAllByContract_Driver_Id(Long driverId);
    List<Request> findAllByClient_IdAndTrip_Status_Status(Long clientId, TripStatusType tripStatus);
    List<Request> findAllByContract_Driver_IdAndTrip_Status_Status(Long driverId, TripStatusType tripStatus);

    List<Request> findAllByContract_Driver_IdAndTrip_Status_StatusIsNot(Long driverId, TripStatusType tripStatus);

    @Query(value = """
        SELECT DISTINCT u FROM user u
        JOIN driver dr ON dr.user_id = u.id
        JOIN vehicle v ON v.driver_id = dr.id
        JOIN vehicle_service vs ON vs.vehicle_id = v.id
        JOIN service s ON s.id = vs.service_id
        WHERE s.id = :serviceId
    """, nativeQuery = true)
    List<User> findUserDriversIdsByServiceId(@Param("serviceId") Long serviceId);

}