package com.techcompany.fastporte.security.domain.model.commands;

public record CreateSafetyThresholdCommand(
        Long sensorTypeId,
        Double maxThreshold,
        Double minThreshold,
        Long tripId,
        Long supervisorId
) {
}
