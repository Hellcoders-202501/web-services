package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import com.techcompany.fastporte.security.domain.model.queries.GetSafetyThresholdByTripIdQuery;

import java.util.List;

public interface SafetyThresholdQueryService {
    List<SafetyThreshold> handle(GetSafetyThresholdByTripIdQuery query);
}
