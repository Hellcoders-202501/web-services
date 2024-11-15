package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.application.dtos.TripInformationDto;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;

public class TripInformationResourceFromEntityAssembler {

    public static TripInformationResource toResourceFromEntity(Trip trip){
        return new TripInformationResource(
                trip.getId(),
                trip.getDriverId(),
                null, //trip.getDriver().getUser().getName(),
                null, //trip.getDriver().getUser().getPhoneNumber(),
                trip.getSupervisorId(),
                null, //trip.getSupervisor().getUser().getName(),
                null,
                trip.getOrigin(),
                trip.getDestination(),
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getStatus().getStatus().name()

        );
    }

    public static TripInformationResource toResourceFromDto(TripInformationDto trip){
        return new TripInformationResource(
                trip.getTripId(),
                trip.getDriverId(),
                trip.getDriverName(),
                trip.getDriverPhoneNumber(),
                trip.getSupervisorId(),
                trip.getSupervisorName(),
                trip.getSupervisorPhoneNumber(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getStatus().name()
        );
    }
}
