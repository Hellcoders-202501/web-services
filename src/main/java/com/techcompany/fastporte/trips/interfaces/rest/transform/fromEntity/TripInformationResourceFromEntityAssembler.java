package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.application.dtos.TripCreatedDto;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;

public class TripInformationResourceFromEntityAssembler {

    public static TripInformationResource toResourceFromEntity(Trip trip){
        return new TripInformationResource(
                trip.getId(),
                null, //trip.getDriverId(),
                null, //trip.getDriver().getUser().getName(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getStatus().getStatus().name()

        );
    }

    public static TripInformationResource toResourceFromDto(TripCreatedDto trip){
        return new TripInformationResource(
                trip.getTripId(),
                trip.getDriverId(),
                trip.getDriverName(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getStatus().name()
        );
    }
}
