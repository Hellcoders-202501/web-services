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
        String type,
        String amount,
        String weight,
        String date,
        String startTime,
        String endTime,
        String subject,
        String description,
        String status
) {
}
