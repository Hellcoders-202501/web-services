package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllServiceTypesQuery;

import java.util.List;

public interface ServiceQueryService  {
    List<Service> handle(GetAllServiceTypesQuery query);
}
