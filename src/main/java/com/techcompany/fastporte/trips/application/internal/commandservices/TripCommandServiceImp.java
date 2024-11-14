package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcompany.fastporte.shared.dtos.DriverInformationDto;
import com.techcompany.fastporte.shared.dtos.SupervisorInformationDto;
import com.techcompany.fastporte.trips.application.dtos.TripInformationDto;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.Status;
import com.techcompany.fastporte.trips.domain.model.commands.*;
import com.techcompany.fastporte.trips.domain.services.TripCommandService;
import com.techcompany.fastporte.trips.infrastructure.acl.DriverAcl;
import com.techcompany.fastporte.trips.infrastructure.acl.SupervisorAcl;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.NotificationRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripRepository;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.TripStatusRepository;
import com.techcompany.fastporte.trips.interfaces.websocket.NotificationWebSocketHandler;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TripCommandServiceImp implements TripCommandService {

    private final TripRepository tripRepository;
    private final TripStatusRepository tripStatusRepository;
    private final NotificationRepository notificationRepository;
    private final DriverRepository driverRepository;
    private final SupervisorRepository supervisorRepository;
    private final DriverAcl driverAcl;
    private final SupervisorAcl supervisorAcl;
    private final NotificationWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public TripCommandServiceImp(TripRepository tripRepository, TripStatusRepository tripStatusRepository, NotificationRepository notificationRepository, UserRepository userRepository, DriverRepository driverRepository, SupervisorRepository supervisorRepository, DriverAcl driverAcl, SupervisorAcl supervisorAcl, NotificationWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.tripRepository = tripRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.notificationRepository = notificationRepository;
        this.driverRepository = driverRepository;
        this.supervisorRepository = supervisorRepository;
        this.driverAcl = driverAcl;
        this.supervisorAcl = supervisorAcl;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<TripInformationDto> handle(CreateTripCommand command) {

        // Mapear el objeto TripRegisterDto a un objeto Trip
        Trip trip = new Trip(command);

        // Asignar el status del viaje
        TripStatus tripStatus = tripStatusRepository.findByStatus(Status.PENDING)
                .orElseThrow(() -> new RuntimeException("Error: El status del viaje no existe en la base de datos"));

        trip.setStatus(tripStatus);

        // Verificar si el conductor existe
        Optional<DriverInformationDto> driver = driverAcl.findDriverById(trip.getDriverId());

        // Verificar si el supervisor existe
        Optional<SupervisorInformationDto> supervisor = supervisorAcl.findSupervisorById(trip.getSupervisorId());

        if (driver.isPresent() && supervisor.isPresent()) {
            // Persistir el viaje
            trip = tripRepository.save(trip);

            // Mapear el objeto Trip a un objeto TripCreatedDto
            TripInformationDto tripInformationDto = TripInformationDto.builder()
                    .tripId(trip.getId())
                    .driverId(driver.get().id())
                    .driverName(driver.get().name() + " " + driver.get().firstLastName() + " " + driver.get().secondLastName())
                    .supervisorId(supervisor.get().id())
                    .supervisorName(supervisor.get().name() + " " + supervisor.get().firstLastName() + " " + supervisor.get().secondLastName())
                    .origin(trip.getOrigin())
                    .destination(trip.getDestination())
                    .startTime(trip.getStartTime())
                    .endTime(trip.getEndTime())
                    .status(trip.getStatus().getStatus())
                    .build();

            // Notificar al supervisor
            SaveNotification(trip.getId(), NotificationType.TRIP_CREATED);

            // Notificar al conductor
            SaveNotification(trip.getId(), NotificationType.TRIP_ASSIGNED);

            return Optional.of(tripInformationDto);

        } else {
            throw new RuntimeException("Error: El conductor con el id '" + trip.getDriverId() + "' no existe en la base de datos");
        }
    }

    @Override
    public void handle(DeleteTripCommand command) {
        tripRepository.deleteById(command.tripId());
    }

    @Override
    public void handle(StartTripCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(Status.IN_PROGRESS);
        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));

        SaveNotification(command.tripId(), NotificationType.TRIP_STARTED);
    }

    @Override
    public void handle(FinishTripCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(Status.FINISHED);
        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));

        SaveNotification(command.tripId(), NotificationType.TRIP_FINISHED);
    }

    @Override
    public void handle(CancelTripCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(Status.CANCELED);
        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));

        SaveNotification(command.tripId(), NotificationType.TRIP_CANCELLED);
    }

    public void SaveNotification(Long tripId, NotificationType notificationType) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if(trip.isEmpty()) {
            throw new RuntimeException("Error: The trip with id '" + tripId + "' does not exist in the database");
        }

        Optional<Driver> driver = driverRepository.findById(trip.get().getDriverId());
        Optional<Supervisor> supervisor = supervisorRepository.findById(trip.get().getSupervisorId());

        if (driver.isEmpty() || supervisor.isEmpty()) {
            throw new RuntimeException("Error: The driver or supervisor with id '" + trip.get().getDriverId() + "' or '" + trip.get().getSupervisorId() + "' does not exist in the database");
        }

        if(notificationType != NotificationType.TRIP_CREATED) {
            // Notify the driver
            Notification notification = Notification.builder()
                    .timestamp(LocalDateTime.now())
                    .type(notificationType)
                    .seen(false)
                    .user(driver.get().getUser())
                    .trip(trip.get())
                    .build();

            notificationRepository.save(notification);

            // Enviar notificación en tiempo real al conductor
            Long driverUserId = driver.get().getUser().getId();
            RoleName role = driver.get().getUser().getRoles().stream().findFirst().get().getRoleName();
            System.out.println("Driver user id: " + driverUserId);
            if (webSocketHandler.isUserConnected(driverUserId, role)) {
                try {
                    String notificationJson = objectMapper.writeValueAsString(notification);
                    webSocketHandler.sendNotification(notificationJson, driverUserId);
                    System.out.println("Notification sent to driver");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(notificationType != NotificationType.TRIP_ASSIGNED) {
            // Notify the supervisor
            Notification notification = Notification.builder()
                    .timestamp(LocalDateTime.now())
                    .type(notificationType)
                    .seen(false)
                    .user(supervisor.get().getUser())
                    .trip(trip.get())
                    .build();

            notificationRepository.save(notification);

            // Enviar notificación en tiempo real al supervisor
            Long supervisorUserId = supervisor.get().getUser().getId();
            RoleName role = supervisor.get().getUser().getRoles().stream().findFirst().get().getRoleName();
            System.out.println("Supervisor user id: " + supervisorUserId);
            System.out.println("Role: " + role);
            if (webSocketHandler.isUserConnected(supervisorUserId, role)) {
                System.out.println("User is connected.....");
                try {
                    String notificationJson = objectMapper.writeValueAsString(notification);
                    webSocketHandler.sendNotification(notificationJson, supervisorUserId);
                    System.out.println("Notification sent to supervisor");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
