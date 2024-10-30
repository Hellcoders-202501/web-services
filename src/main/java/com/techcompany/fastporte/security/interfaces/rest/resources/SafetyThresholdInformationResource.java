package com.techcompany.fastporte.security.interfaces.rest.resources;

public record SafetyThresholdInformationResource(
        Long id,
        String sensorType,
        Double maxThreshold,
        Double minThreshold,
        Long tripId
) {
}