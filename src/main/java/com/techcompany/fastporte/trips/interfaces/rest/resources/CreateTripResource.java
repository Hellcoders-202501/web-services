package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record CreateTripResource(
        Long driverId,
        String origin,
        String destination,
        String startTime,
        String endTime
) {
}
