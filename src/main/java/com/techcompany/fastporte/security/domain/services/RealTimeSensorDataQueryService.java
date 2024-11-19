package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import com.techcompany.fastporte.security.domain.model.queries.GetRealTimeSensorDataQuery;

import java.util.List;

public interface RealTimeSensorDataQueryService {
    List<RealTimeSensorData> handle(GetRealTimeSensorDataQuery query);
}
