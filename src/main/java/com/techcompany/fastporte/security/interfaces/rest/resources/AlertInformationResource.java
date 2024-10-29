package com.techcompany.fastporte.security.interfaces.rest.resources;

public record AlertInformationResource(
        Long id,
        String alertLevel,
        String message,
        CreateAlertResource sensorData
//        Long tripId,
//        String sensorType,
//        Double value,
//        String timestamp
) {
}