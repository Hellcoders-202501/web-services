package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplicationsListResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;

import java.util.List;

public class TripInformationResourceFromEntityAssembler {

    public static TripInformationResource toResourceFromEntity(Trip trip){

        String date = trip.getDate() == null ? "" : trip.getDate().toString();
        String status = trip.getStatus() == null ? null : trip.getStatus().toString();

        return new TripInformationResource(
                trip.getId(),
                trip.getOrigin(),
                trip.getDestination(),
                date,
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getAmount().toString(),
                trip.getSubject(),
                trip.getDescription(),
                status
        );
    }
}
