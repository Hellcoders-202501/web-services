package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.PaymentStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.AcceptDriverApplicationCommand;
import com.techcompany.fastporte.trips.domain.services.ContractCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.*;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContractCommandServiceImp implements ContractCommandService {

    private final ContractRepository contractRepository;
    private final RequestRepository requestRepository;
    private final PaymentRepository paymentRepository;
    private final ApplicationRepository applicationRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final TripStatusRepository tripStatusRepository;

    public ContractCommandServiceImp(ContractRepository contractRepository, RequestRepository requestRepository, DriverRepository driverRepository, PaymentRepository paymentRepository, ApplicationRepository applicationRepository, PaymentStatusRepository paymentStatusRepository, RequestStatusRepository requestStatusRepository, TripStatusRepository tripStatusRepository) {
        this.contractRepository = contractRepository;
        this.requestRepository = requestRepository;
        this.paymentRepository = paymentRepository;
        this.applicationRepository = applicationRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.tripStatusRepository = tripStatusRepository;
    }

    @Override
    public void handle(AcceptDriverApplicationCommand command) {

        Application application = applicationRepository.findById(command.applicationId()).orElse(null);

        if (application == null) {
            throw new RuntimeException("Aplicación no encontrada");
        }

        Request request = application.getRequest();
        Driver driver = application.getDriver();

        ///  Cambiar estado de solicitud a TAKEN
        RequestStatus requestStatus = requestStatusRepository.findByStatusEquals(RequestStatusType.TAKEN).get();
        request.setStatus(requestStatus);

        /// Cambiar el estado del viaje a PENDING
        TripStatus tripStatus = tripStatusRepository.findByStatus(TripStatusType.PENDING).get();
        request.getTrip().setAmount(application.getProposedAmount());
        request.getTrip().setStatus(tripStatus);

        /// Guardar estados cambiados de solicitud y viaje
        request = requestRepository.save(request);

        ///  Crear el contrato
        Contract contract = new Contract();
        contract.setRequest(request);
        contract.setDriver(driver);

        contract = contractRepository.save(contract);

        /// Crear el objeto de pagos
        PaymentStatus paymentStatus = paymentStatusRepository.findByStatus(PaymentStatusType.PENDING_APPROVAL);
        Payment payment = new Payment();
        payment.setAmount(application.getProposedAmount());
        payment.setContract(contract);
        payment.setStatus(paymentStatus);

        paymentRepository.save(payment);
    }
}
