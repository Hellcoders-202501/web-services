package com.techcompany.fastporte.security.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.security.domain.model.commands.UpdateSafetyThresholdCommand;
import com.techcompany.fastporte.security.interfaces.rest.resources.UpdateSafetyThresholdResource;

public class UpdateSafetyThresholdCommandFromResourceAssembler {

    public static UpdateSafetyThresholdCommand toCommandFromResource(UpdateSafetyThresholdResource resource) {
        return new UpdateSafetyThresholdCommand(
                resource.safetyThresholdId(),
                resource.maxThreshold(),
                resource.minThreshold()
        );
    }
}
