package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.queries.*;
import com.techcompany.fastporte.trips.domain.services.TripQueryService;
import com.techcompany.fastporte.trips.infrastructure.acl.DriverAcl;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TripQueryServiceImp implements TripQueryService {

    private final TripRepository tripRepository;
    private final DriverAcl driverAcl;

    private final Logger logger = LoggerFactory.getLogger(TripQueryServiceImp.class);

    public TripQueryServiceImp(TripRepository tripRepository, DriverAcl driverAcl) {
        this.tripRepository = tripRepository;
        this.driverAcl = driverAcl;
    }

    @Override
    public Optional<Trip> handle(GetTripByIdQuery query) {

        return tripRepository.findById(query.tripId());
    }

    @Override
    public List<Trip> handle(GetAllTripsQuery query) {
        return tripRepository.findAll();
    }

    @Override
    public Boolean handle(CheckTripExistsByIdQuery query) {
        return tripRepository.existsById(query.id());
    }

    @Override
    public List<Trip> handle(GetTripsByClientIdQuery query) {
        return tripRepository.findAllByClientId(query.clientId());
    }

    @Override
    public List<Trip> handle(GetTripsByDriverIdQuery query) {
        return tripRepository.findAllByDriverId(query.driverId());
    }

    @Override
    public List<Trip> handle(GetTripsByDriverIdAndStatusQuery query) {
        return tripRepository.findAllByDriverIdAndStatus_Id(query.driverId(), query.statusId());
    }

    @Override
    public List<Trip> handle(GetTripsByClientIdAndStatusQuery query) {
        return tripRepository.findAllByClientIdAndStatus_Id(query.clientId(), query.statusId());
    }
}
