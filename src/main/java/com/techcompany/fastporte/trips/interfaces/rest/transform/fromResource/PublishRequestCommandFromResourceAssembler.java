package com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.commands.PublishRequestCommand;
import com.techcompany.fastporte.trips.interfaces.rest.resources.CreateTripResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.PublishRequestResource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PublishRequestCommandFromResourceAssembler {

    public static PublishRequestCommand toCommandFromResource(PublishRequestResource resource) {

        CreateTripResource res = resource.trip();
        // Formatear a dd-MM-yyyy
        LocalDate date = LocalDate.parse(res.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Formatear hora a HH:mm
        String startTime = res.startTime().length() == 4 ? "0" + res.startTime() : res.startTime();
        String endTime = res.endTime().length() == 4 ? "0" + res.endTime() : res.endTime();


        Trip trip = new Trip();
        trip.setOrigin(res.origin());
        trip.setDestination(res.destination());
        trip.setDate(date);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);
        trip.setAmount(res.amount());
        trip.setSubject(res.subject());
        trip.setDescription(res.description());

        return new PublishRequestCommand(
                resource.clientId(),
                resource.serviceId(),
                trip
        );
    }
}
