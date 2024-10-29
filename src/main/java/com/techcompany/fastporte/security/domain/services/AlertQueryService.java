package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.domain.model.queries.GetAlertsByTripIdQuery;

import java.util.List;

public interface AlertQueryService {
    List<Alert> handle(GetAlertsByTripIdQuery query);
}
