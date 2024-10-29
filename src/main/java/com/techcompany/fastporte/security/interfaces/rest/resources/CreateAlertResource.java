package com.techcompany.fastporte.security.interfaces.rest.resources;

public record CreateAlertResource(
        String sensorType,
        Double value,
        String timestamp,
        Long tripId

) {
}
