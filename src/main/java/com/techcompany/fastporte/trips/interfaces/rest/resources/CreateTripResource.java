package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record CreateTripResource(
        Long driverId,
        Long supervisorId,
        String origin,
        String destination,
        String type,
        String amount,
        String weight,
        String date,
        String startTime,
        String endTime,
        String subject,
        String description
) {
}
