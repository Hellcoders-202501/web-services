package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.shared.utils.Helper;
import com.techcompany.fastporte.shared.websocket.SendNotificationWS;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractCommandServiceImp implements ContractCommandService {

    private final ContractRepository contractRepository;
    private final RequestRepository requestRepository;
    private final PaymentRepository paymentRepository;
    private final ApplicationRepository applicationRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final TripRepository tripRepository;
    private final TripStatusRepository tripStatusRepository;
    private final SendNotificationWS sendNotificationWS;

    public ContractCommandServiceImp(ContractRepository contractRepository, RequestRepository requestRepository, DriverRepository driverRepository, PaymentRepository paymentRepository, ApplicationRepository applicationRepository, PaymentStatusRepository paymentStatusRepository, RequestStatusRepository requestStatusRepository, TripRepository tripRepository, TripStatusRepository tripStatusRepository, SendNotificationWS sendNotificationWS) {
        this.contractRepository = contractRepository;
        this.requestRepository = requestRepository;
        this.paymentRepository = paymentRepository;
        this.applicationRepository = applicationRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.tripRepository = tripRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.sendNotificationWS = sendNotificationWS;
    }

    @Override
    public void handle(AcceptDriverApplicationCommand command) {

        Application application = applicationRepository.findById(command.applicationId()).orElse(null);

        if (application == null) {
            throw new RuntimeException("Aplicación no encontrada");
        }

        Request request = application.getRequest();
        Driver driver = application.getDriver();

        /// Verificar que el conductor no tenga contratos en el horario de la solicitud a la que va a aplicar
        List<Trip> tripsList = tripRepository.findAllByRequest_Contract_Driver_IdAndStatus_StatusIsNotAndStatus_StatusIsNotNull(driver.getId(), TripStatusType.COMPLETED);

        LocalDateTime requestStart = LocalDateTime.of(request.getTrip().getDate(), LocalTime.parse(request.getTrip().getStartTime()));
        LocalDateTime requestEnd = LocalDateTime.of(request.getTrip().getDate(), LocalTime.parse(request.getTrip().getEndTime()));

        List<Long> overlappingRequestIds = Helper.VerifyOverlappingRequests(tripsList, requestStart, requestEnd);

        if (!overlappingRequestIds.isEmpty()) {

            throw new RuntimeException("El conductor actualmente tiene viajes pendientes en el horario de la solicitud.");
        }

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

        /// Envío de notificación de creación de contrato
        SaveContractNotification(contract.getId(), NotificationType.CONTRACT_CREATED);

        /// Crear el objeto de pagos
        PaymentStatus paymentStatus = paymentStatusRepository.findByStatus(PaymentStatusType.PENDING_APPROVAL);
        Payment payment = new Payment();
        payment.setAmount(application.getProposedAmount());
        payment.setContract(contract);
        payment.setStatus(paymentStatus);

        paymentRepository.save(payment);
    }

    public void SaveContractNotification(Long contractId, NotificationType notificationType) {
        Optional<Contract> contract = contractRepository.findById(contractId);

        if(contract.isEmpty()) {
            throw new RuntimeException("Error: El contrato con id '" + contractId + "' no existe.");
        }

        Long driverId = contract.get().getDriver().getId();
        Long clientId = contract.get().getRequest().getClient().getId();

        sendNotificationWS.notifyDriverAndClient(driverId, clientId, notificationType, contractId);
    }
}
