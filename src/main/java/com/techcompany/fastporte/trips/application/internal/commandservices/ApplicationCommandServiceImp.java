package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.shared.utils.Helper;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Contract;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.ApplyToRequestCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteApplicationCommand;
import com.techcompany.fastporte.trips.domain.services.ApplicationCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ApplicationRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ContractRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationCommandServiceImp implements ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final DriverRepository driverRepository;
    private final RequestRepository requestRepository;
    private final TripRepository tripRepository;

    public ApplicationCommandServiceImp(ApplicationRepository applicationRepository, DriverRepository driverRepository, RequestRepository requestRepository, ContractRepository contractRepository, TripRepository tripRepository) {
        this.applicationRepository = applicationRepository;
        this.driverRepository = driverRepository;
        this.requestRepository = requestRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void handle(ApplyToRequestCommand command) {

        Optional<Driver> driver = driverRepository.findById(command.driverId());
        Optional<Request> request = requestRepository.findById(command.requestId());

        /// Verificar que la solicitud aún no ha sido tomada
        if (request.get().getStatus().getStatus() == RequestStatusType.TAKEN) {
            throw new RuntimeException("La solicitud ya ha sido tomada");
        }

        /// Verificar que el conductor no tenga contratos en el horario de la solicitud a la que va a aplicar
        List<Trip> tripsList = tripRepository.findAllByRequest_Contract_Driver_IdAndStatus_StatusIsNotAndStatus_StatusIsNotNull(command.driverId(), TripStatusType.COMPLETED);

        LocalDateTime requestStart = LocalDateTime.of(request.get().getTrip().getDate(), LocalTime.parse(request.get().getTrip().getStartTime()));
        LocalDateTime requestEnd = LocalDateTime.of(request.get().getTrip().getDate(), LocalTime.parse(request.get().getTrip().getEndTime()));

        List<Long> overlappingRequestIds = Helper.VerifyOverlappingRequests(tripsList, requestStart, requestEnd);

        if (!overlappingRequestIds.isEmpty()) {

            String ids = overlappingRequestIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            throw new RuntimeException("El conductor ya tiene viajes pendientes en el horario de la solicitud a la que desea aplicar. IDs de solicitudes cruzadas: " + ids);
        }

        Application application = new Application(
                command.message(),
                command.proposedAmount(),
                request.get(),
                driver.get()
        );

        applicationRepository.save(application);
        //Todo: Agregar notificacion
    }

    @Override
    public void handle(DeleteApplicationCommand command) {

        Application application = applicationRepository.findById(command.id()).orElse(null);

        if (application == null) {
            throw new RuntimeException("La aplicación para esta solicitud no existe. Id: " + command.id());
        }

        applicationRepository.deleteById(command.id());

        //Todo: Agregar notificación
    }
}
