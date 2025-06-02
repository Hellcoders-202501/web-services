package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record TripInformationResource(
        Long id,
        String origin,
        String destination,
        String date,
        String startTime,
        String endTime,
        String amount,
        String subject,
        String description,
        String status
) {
}
