package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.ApplyToRequestCommand;
import com.techcompany.fastporte.trips.domain.services.ApplicationCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ApplicationRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ApplicationCommandServiceImp implements ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final DriverRepository driverRepository;
    private final RequestRepository requestRepository;

    public ApplicationCommandServiceImp(ApplicationRepository applicationRepository, DriverRepository driverRepository, RequestRepository requestRepository) {
        this.applicationRepository = applicationRepository;
        this.driverRepository = driverRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void handle(ApplyToRequestCommand command) {

        Optional<Driver> driver = driverRepository.findById(command.driverId());
        Optional<Request> request = requestRepository.findById(command.requestId());

        if (request.get().getStatus().getStatus() == RequestStatusType.TAKEN) {
            throw new RuntimeException("La solicitud ya ha sido tomada");
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
}
