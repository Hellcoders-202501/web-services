package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllTripStatusQuery;
import com.techcompany.fastporte.trips.domain.services.TripStatusQueryService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TripStatusQueryServiceImp implements TripStatusQueryService {

    private final TripStatusRepository tripStatusRepository;

    public TripStatusQueryServiceImp(TripStatusRepository tripStatusRepository) {
        this.tripStatusRepository = tripStatusRepository;
    }

    @Override
    public List<TripStatus> handle(GetAllTripStatusQuery query) {
        return tripStatusRepository.findAll();
    }
}
