package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripStatusResource;

public class TripStatusResourceFromEntityAssembler {
    public static TripStatusResource fromEntity(TripStatus tripStatus) {
        return new TripStatusResource(
                tripStatus.getId(),
                tripStatus.getStatus().toString()
        );
    }
}
