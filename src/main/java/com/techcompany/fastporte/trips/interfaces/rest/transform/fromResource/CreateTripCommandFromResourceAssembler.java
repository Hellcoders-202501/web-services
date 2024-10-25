package com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import com.techcompany.fastporte.trips.interfaces.rest.resources.CreateTripResource;

public class CreateTripCommandFromResourceAssembler {
    public static CreateTripCommand toCommandFromResource(CreateTripResource resource) {
        return new CreateTripCommand(
                resource.driverId(),
                resource.origin(),
                resource.destination(),
                resource.startTime(),
                resource.endTime()
        );
    }
}
