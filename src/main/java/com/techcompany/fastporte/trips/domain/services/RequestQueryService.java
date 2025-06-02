package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByClientIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByServiceIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetRequestByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RequestQueryService {

    List<Request> handle(GetAllRequestsByServiceIdAndNotTakenQuery query);
    List<Request> handle(GetAllRequestsByClientIdAndNotTakenQuery query);
    Optional<Request> handle(GetRequestByIdQuery query);
}