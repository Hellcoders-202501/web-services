package com.techcompany.fastporte.security.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import com.techcompany.fastporte.security.interfaces.rest.resources.SafetyThresholdInformationResource;

public class SafetyThresholdInformationResourceFromEntityAssembler {

    public static SafetyThresholdInformationResource toResourceFromEntity(SafetyThreshold safetyThreshold) {
        return new SafetyThresholdInformationResource(
                safetyThreshold.getId(),
                safetyThreshold.getSensorType().getType().name(),
                safetyThreshold.getMaxThreshold(),
                safetyThreshold.getMinThreshold(),
                safetyThreshold.getTripId()
        );
    }
}
