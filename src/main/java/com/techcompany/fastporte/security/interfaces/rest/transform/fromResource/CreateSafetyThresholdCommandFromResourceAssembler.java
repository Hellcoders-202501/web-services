package com.techcompany.fastporte.security.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.security.domain.model.commands.CreateSafetyThresholdCommand;
import com.techcompany.fastporte.security.interfaces.rest.resources.CreateSafetyThresholdResource;

public class CreateSafetyThresholdCommandFromResourceAssembler {

    public static CreateSafetyThresholdCommand toCommandFromResource(CreateSafetyThresholdResource resource) {
        return new CreateSafetyThresholdCommand(
                resource.sensorTypeId(),
                resource.maxThreshold(),
                resource.minThreshold(),
                resource.tripId(),
                resource.clientId()
        );
    }
}
