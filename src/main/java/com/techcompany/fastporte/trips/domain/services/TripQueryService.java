package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface TripQueryService {

    Optional<Request> handle(GetTripByIdQuery query);
    List<Request> handle(GetAllTripsQuery query);
    Boolean handle(CheckTripExistsByIdQuery query);

    //List<Trip> findAllByStatus(Long statusId);
    //List<Trip> findAllByDriverIdAndSupervisorId(Long driverId, Long clientId);

    List<Request> handle(GetTripsByClientIdQuery query);
    List<Request> handle(GetTripsByDriverIdQuery query);
    List<Request> handle(GetTripsByDriverIdAndStatusQuery query);
    List<Request> handle(GetTripsByClientIdAndStatusQuery query);
}