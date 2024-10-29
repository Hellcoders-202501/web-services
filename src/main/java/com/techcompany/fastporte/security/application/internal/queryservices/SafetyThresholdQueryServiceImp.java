package com.techcompany.fastporte.security.application.internal.queryservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import com.techcompany.fastporte.security.domain.model.queries.GetSafetyThresholdByTripIdQuery;
import com.techcompany.fastporte.security.domain.services.SafetyThresholdQueryService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SafetyThresholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SafetyThresholdQueryServiceImp implements SafetyThresholdQueryService {

    private final SafetyThresholdRepository safetyThresholdRepository;

    public SafetyThresholdQueryServiceImp(SafetyThresholdRepository safetyThresholdRepository) {
        this.safetyThresholdRepository = safetyThresholdRepository;
    }

    @Override
    public List<SafetyThreshold> handle(GetSafetyThresholdByTripIdQuery query) {
        return safetyThresholdRepository.findByTripId(query.tripId());
    }
}
