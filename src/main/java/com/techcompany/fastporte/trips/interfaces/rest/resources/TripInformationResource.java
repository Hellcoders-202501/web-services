package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record TripInformationResource(
        Long tripId,
        Long driverId,
        String driverName,
        String driverPhoneNumber,
        Long supervisorId,
        String supervisorName,
        String supervisorPhoneNumber,
        String origin,
        String destination,
        String startTime,
        String endTime,
        String status
) {
}
