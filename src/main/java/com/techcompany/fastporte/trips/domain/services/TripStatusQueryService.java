package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllTripStatusQuery;

import java.util.List;

public interface TripStatusQueryService {
    List<TripStatus> handle(GetAllTripStatusQuery query);
}
