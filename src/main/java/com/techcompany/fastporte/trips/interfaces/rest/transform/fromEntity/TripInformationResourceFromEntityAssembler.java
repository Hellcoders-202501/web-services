package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;

public class TripInformationResourceFromEntityAssembler {

    public static TripInformationResource toResourceFromEntity(Trip trip){

        String driverName = trip.getDriver().getUser().getName() + " " + trip.getDriver().getUser().getFirstLastName() + " " + trip.getDriver().getUser().getSecondLastName();
        String supervisorName = trip.getSupervisor().getUser().getName() + " " + trip.getSupervisor().getUser().getFirstLastName() + " " + trip.getSupervisor().getUser().getSecondLastName();

        String date = trip.getDate() == null ? "" : trip.getDate().toString();

        return new TripInformationResource(
                trip.getId(),
                trip.getDriver().getId(),
                driverName,
                trip.getDriver().getUser().getPhone(),
                trip.getSupervisor().getId(),
                supervisorName,
                trip.getSupervisor().getUser().getPhone(),
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
