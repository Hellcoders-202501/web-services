package com.techcompany.fastporte.security.interfaces.rest.resources;

public record CreateSafetyThresholdResource(
        Long sensorTypeId,
        Double maxThreshold,
        Double minThreshold,
        Long tripId,
        Long supervisorId
) {
}
