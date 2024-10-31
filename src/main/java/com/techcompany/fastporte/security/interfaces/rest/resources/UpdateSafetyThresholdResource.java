package com.techcompany.fastporte.security.interfaces.rest.resources;

public record UpdateSafetyThresholdResource(
        Long safetyThresholdId,
        Double maxThreshold,
        Double minThreshold
) {
}
