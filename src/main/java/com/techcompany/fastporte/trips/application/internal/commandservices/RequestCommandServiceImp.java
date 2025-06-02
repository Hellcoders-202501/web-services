package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.RequestStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteRequestCommand;
import com.techcompany.fastporte.trips.domain.model.commands.PublishRequestCommand;
import com.techcompany.fastporte.trips.domain.services.RequestCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestStatusRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ServiceRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripStatusRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RequestCommandServiceImp implements RequestCommandService {

    private final RequestRepository requestRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final TripStatusRepository tripStatusRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;

    public RequestCommandServiceImp(RequestRepository requestRepository, RequestStatusRepository requestStatusRepository, TripStatusRepository tripStatusRepository, ClientRepository clientRepository, ServiceRepository serviceRepository) {
        this.requestRepository = requestRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Optional<Request> handle(PublishRequestCommand command) {

        Optional<RequestStatus> requestStatus = requestStatusRepository.findByStatusEquals(RequestStatusType.PUBLISHED);
        Optional<Client> client = clientRepository.findById(command.clientId());
        Optional<com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service> service = serviceRepository.findById(command.serviceId());

        if (requestStatus.isPresent() && client.isPresent() && service.isPresent()) {

            command.trip().setStatus(null);

            Request request = new Request();
            request.setClient(client.get());
            request.setService(service.get());
            request.setStatus(requestStatus.get());
            request.setTrip(command.trip());
            request.setContract(null);
            request.setApplications(null);

            return Optional.of(requestRepository.save(request));

        } else {
            String mensajeError = "";

            if (requestStatus.isEmpty()) mensajeError += "Estado de solicitud no encontrado. ";
            if (client.isEmpty()) mensajeError += "Cliente no encontrado. ";
            if (service.isEmpty()) mensajeError += "Servicio no encontrado. ";

            throw new IllegalArgumentException("No se puede publicar la solicitud: " + mensajeError.trim());
        }
    }

    @Override
    public void handle(DeleteRequestCommand command) {

        Optional<Request> request = requestRepository.findById(command.requestId());

        if (request.isPresent() && request.get().getStatus().getStatus().equals(RequestStatusType.PUBLISHED)) {
            requestRepository.deleteById(command.requestId());
        }
        else  {
            throw new IllegalArgumentException("No se puede eliminar una solicitud que ya tiene contrato: " + command.requestId());
        }

    }
}
