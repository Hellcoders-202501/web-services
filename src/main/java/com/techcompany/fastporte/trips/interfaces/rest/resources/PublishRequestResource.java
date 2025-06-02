package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record PublishRequestResource(
        Long clientId,
        Long serviceId,
        CreateTripResource trip
) {
}
