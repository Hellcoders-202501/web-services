package com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import com.techcompany.fastporte.trips.interfaces.rest.resources.CreateTripResource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateTripCommandFromResourceAssembler {
    public static CreateTripCommand toCommandFromResource(CreateTripResource resource) {

        // Formatear a dd-MM-yyyy
        LocalDate date = LocalDate.parse(resource.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Formatear hora a HH:mm
        String startTime = resource.startTime().length() == 4 ? "0" + resource.startTime() : resource.startTime();
        String endTime = resource.endTime().length() == 4 ? "0" + resource.endTime() : resource.endTime();

        return new CreateTripCommand(
                resource.driverId(),
                resource.clientId(),
                resource.origin(),
                resource.destination(),
                resource.type(),
                resource.amount(),
                resource.weight(),
                date,
                startTime,
                endTime,
                resource.subject(),
                resource.description()
        );
    }
}
