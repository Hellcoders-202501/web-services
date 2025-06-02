package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllApplicationsByRequestIdQuery;

import java.util.List;

public interface ApplicationQueryService {
    List<Application> handle(GetAllApplicationsByRequestIdQuery query);
}
