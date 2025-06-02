package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.queries.*;
import com.techcompany.fastporte.trips.domain.services.TripQueryService;
import com.techcompany.fastporte.trips.infrastructure.acl.DriverAcl;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripStatusRepository;
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
    private final TripStatusRepository tripStatusRepository;
    private final RequestRepository requestRepository;

    private final Logger logger = LoggerFactory.getLogger(TripQueryServiceImp.class);

    public TripQueryServiceImp(TripRepository tripRepository, TripStatusRepository tripStatusRepository, RequestRepository requestRepository) {
        this.tripRepository = tripRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<Request> handle(GetTripByIdQuery query) {

        return requestRepository.findByTrip_Id(query.tripId());
        //return tripRepository.findById(query.tripId());
    }

    @Override
    public List<Request> handle(GetAllTripsQuery query) {

        return requestRepository.findAllByStatus_Status(RequestStatusType.TAKEN);
        //return tripRepository.findAll();
    }

    @Override
    public Boolean handle(CheckTripExistsByIdQuery query) {
        return tripRepository.existsById(query.id());
    }

    @Override
    public List<Request> handle(GetTripsByClientIdQuery query) {

        return requestRepository.findAllByClient_Id(query.clientId());
        //return tripRepository.findAllByClientId(query.clientId());
    }

    @Override
    public List<Request> handle(GetTripsByDriverIdQuery query) {

        return requestRepository.findAllByContract_Driver_Id(query.driverId());
        //return tripRepository.findAllByDriverId(query.driverId());
    }

    @Override
    public List<Request> handle(GetTripsByDriverIdAndStatusQuery query) {

        Optional<TripStatus> tripStatus = tripStatusRepository.findById(query.statusId());

        if (tripStatus.isPresent()) {

            if (tripStatus.get().getStatus() == TripStatusType.PENDING) {

                /// Devuelvo como pendientes a todos los que no están completados
                return requestRepository.findAllByContract_Driver_IdAndTrip_Status_StatusIsNot(query.statusId(), TripStatusType.COMPLETED);
            }

            return requestRepository.findAllByContract_Driver_IdAndTrip_Status_Status(query.driverId(), tripStatus.get().getStatus());
        }
        else {
            String mensajeError = "Estado de viaje no encontrado. ";
            throw new IllegalArgumentException("No se pueden obtener los viajes del conductor: " + mensajeError.trim());
        }

        //return tripRepository.findAllByDriverIdAndStatus_Id(query.driverId(), query.statusId());
    }

    @Override
    public List<Request> handle(GetTripsByClientIdAndStatusQuery query) {

        Optional<TripStatus> tripStatus = tripStatusRepository.findById(query.statusId());

        if (tripStatus.isPresent()) {
            return requestRepository.findAllByClient_IdAndTrip_Status_Status(query.clientId(), tripStatus.get().getStatus());
        }
        else {
            String mensajeError = "Estado de viaje no encontrado.";
            throw new IllegalArgumentException("No se pueden obtener los viajes del cliente: " + mensajeError.trim());
        }

        //return tripRepository.findAllByClientIdAndStatus_Id(query.clientId(), query.statusId());
    }
}
