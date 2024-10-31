package com.techcompany.fastporte.security.domain.model.commands;

public record UpdateSafetyThresholdCommand(
        Long safetyThresholdId,
        Double maxThreshold,
        Double minThreshold
) {
}
