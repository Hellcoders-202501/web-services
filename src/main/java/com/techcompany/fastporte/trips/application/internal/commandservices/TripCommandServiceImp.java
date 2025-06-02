package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.PaymentStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.*;
import com.techcompany.fastporte.trips.domain.services.TripCommandService;
import com.techcompany.fastporte.trips.infrastructure.acl.DriverAcl;
import com.techcompany.fastporte.trips.infrastructure.acl.ClientAcl;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.*;
import com.techcompany.fastporte.trips.infrastructure.websocket.NotificationWebSocketHandler;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
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
    private final ClientRepository clientRepository;
    private final CommentRepository commentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final NotificationWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    public TripCommandServiceImp(TripRepository tripRepository, TripStatusRepository tripStatusRepository, NotificationRepository notificationRepository, UserRepository userRepository, DriverRepository driverRepository, ClientRepository clientRepository, DriverAcl driverAcl, ClientAcl clientAcl, CommentRepository commentRepository, PaymentStatusRepository paymentStatusRepository, NotificationWebSocketHandler webSocketHandler, ObjectMapper objectMapper, PaymentRepository paymentRepository) {
        this.tripRepository = tripRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.notificationRepository = notificationRepository;
        this.driverRepository = driverRepository;
        this.clientRepository = clientRepository;
        this.commentRepository = commentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
    }

//    @Override
//    public Optional<Trip> handle(CreateTripCommand command) {
//
//        // Mapear el objeto TripRegisterDto a un objeto Trip
//        Trip trip = new Trip(command);
//
//        // Asignar el status del viaje
//        TripStatus tripStatus = tripStatusRepository.findByStatus(TripStatusType.PENDING)
//                .orElseThrow(() -> new RuntimeException("Error: El status del viaje no existe en la base de datos"));
//
//        trip.setStatus(tripStatus);
//
//        // Verificar si el conductor existe
//        Optional<Driver> driver = driverRepository.findById(command.driverId());
//
//        // Verificar si el client existe
//        Optional<Client> client = clientRepository.findById(command.clientId());
//
//        if (driver.isPresent() && client.isPresent()) {
//
//            // Asignar el conductor y el client al viaje
//            trip.assignDriver(driver.get());
//            trip.assignClient(client.get());
//
//            // Persistir el viaje
//            trip = tripRepository.save(trip);
//
//            // Notificar al client
//            SaveNotification(trip.getId(), NotificationType.TRIP_CREATED);
//
//            // Notificar al conductor
//            SaveNotification(trip.getId(), NotificationType.TRIP_ASSIGNED);
//
//            return Optional.of(trip);
//
//        } else {
//            throw new RuntimeException("Error: The driver or client with id '" + command.driverId() + "' or '" + command.clientId() + "' does not exist in the database");
//        }
//    }

    @Override
    public void handle(DeleteTripCommand command) {
        tripRepository.deleteById(command.tripId());
    }

    @Override
    public void handle(StartTripCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(TripStatusType.STARTED);
        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));

        SaveTripNotification(command.tripId(), NotificationType.TRIP_STARTED);
    }

    @Override
    public void handle(FinishTripByDriverCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(TripStatusType.FINISHED_BY_DRIVER);

        if (tripStatus.isPresent()) {
            tripRepository.updateStatusById(command.tripId(), tripStatus.get().getId());
        } else {
            throw new RuntimeException("El estado de viaje no existe");
        }

        SaveTripNotification(command.tripId(), NotificationType.TRIP_FINISHED_BY_DRIVER);
    }


    @Override
    public void handle(FinishTripByClientCommand command) {
        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(TripStatusType.FINISHED_BY_CLIENT);

        if (tripStatus.isPresent()) {
            tripRepository.updateStatusById(command.tripId(), tripStatus.get().getId());

            /// Lógica de payment para el conductor
            PaymentStatus paymentStatus = paymentStatusRepository.findByStatus(PaymentStatusType.APPROVED);
            Trip trip = tripRepository.findById(command.tripId()).get();

            Payment payment = trip.getRequest().getContract().getPayment();
            payment.setStatus(paymentStatus);
            payment = paymentRepository.save(payment);

            SaveTripNotification(command.tripId(), NotificationType.TRIP_FINISHED_BY_CLIENT);

            if (payment.getStatus().getStatus() == PaymentStatusType.APPROVED) {
                tripStatus = tripStatusRepository.findByStatus(TripStatusType.COMPLETED);

                trip.setStatus(tripStatus.get());
                tripRepository.updateStatusById(command.tripId(), tripStatus.get().getId());

            } else {

                throw new RuntimeException("No se pudo cambiar el estado del pago porque el pago no está aprobado.");
            }

        } else {
            throw new RuntimeException("El estado de viaje no existe");
        }

    }

//    @Override
//    public void handle(FinishTripCommand command) {
//        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(TripStatusType.COMPLETED);
//        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));
//
//        SaveNotification(command.tripId(), NotificationType.TRIP_COMPLETED);
//    }

//    @Override
//    public void handle(CancelTripCommand command) {
//        Optional<TripStatus> tripStatus = tripStatusRepository.findByStatus(TripStatusType.CANCELED);
//        tripStatus.ifPresent(status -> tripRepository.updateStatusById(command.tripId(), status.getId()));
//
//        SaveNotification(command.tripId(), NotificationType.TRIP_CANCELLED);
//    }

    public void SaveTripNotification(Long tripId, NotificationType notificationType) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if(trip.isEmpty()) {
            throw new RuntimeException("Error: The trip with id '" + tripId + "' does not exist in the database");
        }

        Long driverId = trip.get().getRequest().getContract().getDriver().getId();
        Long clientId = trip.get().getRequest().getClient().getId();

        Optional<Driver> driver = driverRepository.findById(driverId);
        Optional<Client> client = clientRepository.findById(clientId);

        if (driver.isEmpty() || client.isEmpty()) {
            throw new RuntimeException("Error: The driver with id '" + driverId + "' or client with id'" + clientId + "' does not exist in the database");
        }

        // Enviar notificación en tiempo real al conductor
        SendNotificationWS(driver.get().getUser(), notificationType, tripId);

        // Enviar notificación en tiempo real al cliente
        SendNotificationWS(client.get().getUser(), notificationType, tripId);

        /*if(notificationType != NotificationType.TRIP_CREATED) {
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

            if (webSocketHandler.isUserConnected(driverUserId, role)) {
                try {
                    String notificationJson = objectMapper.writeValueAsString(notification.toDto());
                    webSocketHandler.sendNotification(notificationJson, driverUserId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(notificationType != NotificationType.TRIP_ASSIGNED) {
            // Notify the client
            Notification notification = Notification.builder()
                    .timestamp(LocalDateTime.now())
                    .type(notificationType)
                    .seen(false)
                    .user(client.get().getUser())
                    .trip(trip.get())
                    .build();

            notificationRepository.save(notification);

            // Enviar notificación en tiempo real al client
            Long clientUserId = client.get().getUser().getId();
            RoleName role = client.get().getUser().getRoles().stream().findFirst().get().getRoleName();

            if (webSocketHandler.isUserConnected(clientUserId, role)) {
                try {
                    String notificationJson = objectMapper.writeValueAsString(notification.toDto());
                    webSocketHandler.sendNotification(notificationJson, clientUserId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    private void SendNotificationWS(User user, NotificationType notificationType, Long referenceId) {

        // Notify
        Notification notification = Notification.builder()
                .createdAt(LocalDateTime.now())
                .type(notificationType)
                .referenceId(referenceId)
                .seen(false)
                .user(user)
                .build();

        notificationRepository.save(notification);

        Long userId = user.getId();
        RoleName roleClient = user.getRoles().stream().findFirst().get().getRoleName();

        if (webSocketHandler.isUserConnected(userId, roleClient)) {
            try {
                String notificationJson = objectMapper.writeValueAsString(notification.toDto());
                webSocketHandler.sendNotification(notificationJson, userId);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: Something went wrong while sending notification. Exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void handle(AddCommentCommand command) {

        Trip trip = tripRepository.findById(command.tripId()).orElse(null);

        if (trip == null) {
            throw new RuntimeException("Viaje con id " + command.tripId() + " no existe");
        }

        if (trip.getStatus().getStatus() != TripStatusType.FINISHED_BY_CLIENT) {
            throw new RuntimeException("Viaje con id " + command.tripId() + " no está finalizado");
        }

        if (trip.getComment() != null) {
            throw new RuntimeException("El viaje ya tiene un comentario");
        }

        Comment comment = new Comment();
        comment.setTrip(trip);
        comment.setContent(command.content());
        comment.setRating(command.rating());
        commentRepository.save(comment);

        ///  Lógica para calcular el nuevo puntaje del conductor
        Double newRating = commentRepository.calculateAverageRatingForDriver(trip.getRequest().getContract().getDriver().getId());

        Driver driver = trip.getRequest().getContract().getDriver();
        driver.setRating(newRating);
        driverRepository.save(driver);

    }

}
