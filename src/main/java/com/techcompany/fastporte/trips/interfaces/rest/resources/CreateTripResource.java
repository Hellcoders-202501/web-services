package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record CreateTripResource(
        Long driverId,
        Long supervisorId,
        String origin,
        String destination,
        String startTime,
        String endTime
) {
}
