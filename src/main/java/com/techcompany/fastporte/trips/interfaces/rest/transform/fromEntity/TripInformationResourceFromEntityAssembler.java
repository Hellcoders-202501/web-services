package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;

public class TripInformationResourceFromEntityAssembler {

    public static TripInformationResource toResourceFromEntity(Trip trip){

        String driverName = trip.getDriver().getUser().getName() + " " + trip.getDriver().getUser().getFirstLastName() + " " + trip.getDriver().getUser().getSecondLastName();
        String clientName = trip.getClient().getUser().getName() + " " + trip.getClient().getUser().getFirstLastName() + " " + trip.getClient().getUser().getSecondLastName();

        String date = trip.getDate() == null ? "" : trip.getDate().toString();

        return new TripInformationResource(
                trip.getId(),
                trip.getDriver().getId(),
                driverName,
                trip.getDriver().getUser().getPhone(),
                trip.getClient().getId(),
                clientName,
                trip.getClient().getUser().getPhone(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getType(),
                trip.getAmount(),
                trip.getWeight(),
                date,
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getSubject(),
                trip.getDescription(),
                trip.getStatus().getStatus().name()

        );
    }
}
