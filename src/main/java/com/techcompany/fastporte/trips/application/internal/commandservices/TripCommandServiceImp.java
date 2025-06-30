package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.shared.websocket.SendNotificationWS;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.PaymentStatusType;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.TripStatusType;
import com.techcompany.fastporte.trips.domain.model.commands.*;
import com.techcompany.fastporte.trips.domain.services.TripCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.*;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccount;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Transaction;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.BankAccountRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TripCommandServiceImp implements TripCommandService {

    private final TripRepository tripRepository;
    private final TripStatusRepository tripStatusRepository;
    private final DriverRepository driverRepository;
    private final ClientRepository clientRepository;
    private final CommentRepository commentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final SendNotificationWS sendNotificationWS;

    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public TripCommandServiceImp(TripRepository tripRepository, TripStatusRepository tripStatusRepository, DriverRepository driverRepository, ClientRepository clientRepository, CommentRepository commentRepository, PaymentStatusRepository paymentStatusRepository, SendNotificationWS sendNotificationWS, PaymentRepository paymentRepository, BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.tripRepository = tripRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.driverRepository = driverRepository;
        this.clientRepository = clientRepository;
        this.commentRepository = commentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.sendNotificationWS = sendNotificationWS;
        this.paymentRepository = paymentRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

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
                SaveTripNotification(command.tripId(), NotificationType.TRIP_COMPLETED);

                /// Agregar transacción
                Long driverId = trip.getRequest().getContract().getDriver().getId();
                BankAccount bankAccount = bankAccountRepository.findByDriver_id(driverId).get();
                Transaction transaction = new Transaction(bankAccount, trip.getAmount());

                transactionRepository.save(transaction);

            } else {

                throw new RuntimeException("No se pudo cambiar el estado del pago porque el pago no está aprobado.");
            }

        } else {
            throw new RuntimeException("El estado de viaje no existe");
        }

    }

    public void SaveTripNotification(Long tripId, NotificationType notificationType) {
        Optional<Trip> trip = tripRepository.findById(tripId);

        if(trip.isEmpty()) {
            throw new RuntimeException("Error: The trip with id '" + tripId + "' does not exist in the database");
        }

        Long driverId = trip.get().getRequest().getContract().getDriver().getId();
        Long clientId = trip.get().getRequest().getClient().getId();

        sendNotificationWS.notifyDriverAndClient(driverId, clientId, notificationType, tripId);
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
