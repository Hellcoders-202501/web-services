package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.shared.dtos.DriverInformationDto;
import com.techcompany.fastporte.trips.application.dtos.TripInformationDto;
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
    public Optional<TripInformationDto> handle(GetTripByIdQuery query) {

        Optional<Trip> trip = tripRepository.findById(query.tripId());

        if (trip.isPresent()) {
            Optional<DriverInformationDto> driver = driverAcl.findDriverById(trip.get().getDriverId());

            if (driver.isPresent()) {

                logger.info("Driver found: " + driver.get().firstLastName());

               TripInformationDto tripInformationDto = TripInformationDto.builder()
                          .tripId(trip.get().getId())
                          .driverId(driver.get().id())
                          .driverName(driver.get().name())
                          .origin(trip.get().getOrigin())
                          .destination(trip.get().getDestination())
                          .startTime(trip.get().getStartTime())
                          .endTime(trip.get().getEndTime())
                          .status(trip.get().getStatus().getStatus())
                          .build();

                return Optional.of(tripInformationDto);
            } else {

                TripInformationDto tripInformationDto = TripInformationDto.builder()
                          .tripId(trip.get().getId())
                          .driverId(null)
                          .driverName(null)
                          .origin(trip.get().getOrigin())
                          .destination(trip.get().getDestination())
                          .startTime(trip.get().getStartTime())
                          .endTime(trip.get().getEndTime())
                          .build();

                return Optional.of(tripInformationDto);
            }

        } else {
            return Optional.empty();
        }
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
    public List<Trip> handle(GetTripsBySupervisorIdQuery query) {
        return tripRepository.findAllBySupervisorId(query.supervisorId());
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
    public List<Trip> handle(GetTripsBySupervisorIdAndStatusQuery query) {
        return tripRepository.findAllBySupervisorIdAndStatus_Id(query.supervisorId(), query.statusId());
    }
}
