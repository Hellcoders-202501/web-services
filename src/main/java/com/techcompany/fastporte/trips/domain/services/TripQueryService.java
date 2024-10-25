package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.application.dtos.TripInformationDto;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface TripQueryService {

    Optional<TripInformationDto> handle(GetTripByIdQuery query);
    List<Trip> handle(GetAllTripsQuery query);
    Boolean handle(CheckTripExistsByIdQuery query);

    //List<Trip> findAllByStatus(Long statusId);
    //List<Trip> findAllByDriverIdAndSupervisorId(Long driverId, Long supervisorId);

    List<Trip> handle(GetTripsBySupervisorIdQuery query);
    List<Trip> handle(GetTripsByDriverIdQuery query);
    List<Trip> handle(GetTripsByDriverIdAndStatusQuery query);
    List<Trip> handle(GetTripsBySupervisorIdAndStatusQuery query);
}