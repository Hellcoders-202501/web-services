package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record ApplyToRequestResource(
        String message,
        Double proposedAmount,
        Long requestId,
        Long driverId
) {
}
