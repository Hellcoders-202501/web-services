package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import com.techcompany.fastporte.security.domain.model.commands.CreateSafetyThresholdCommand;
import com.techcompany.fastporte.security.domain.model.commands.UpdateSafetyThresholdCommand;

import java.util.Optional;

public interface SafetyThresholdCommandService {
    Optional<SafetyThreshold> handle(CreateSafetyThresholdCommand command);
    Optional<SafetyThreshold> handle(UpdateSafetyThresholdCommand command);
}
