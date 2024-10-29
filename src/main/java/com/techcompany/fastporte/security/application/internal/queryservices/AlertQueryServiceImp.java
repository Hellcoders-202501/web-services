package com.techcompany.fastporte.security.application.internal.queryservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.domain.model.queries.GetAlertsByTripIdQuery;
import com.techcompany.fastporte.security.domain.services.AlertQueryService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlertQueryServiceImp implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImp(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> handle(GetAlertsByTripIdQuery query) {
        return alertRepository.findBySensorData_TripId(query.tripId());
    }
}
