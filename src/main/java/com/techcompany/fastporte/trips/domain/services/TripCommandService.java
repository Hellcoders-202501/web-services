package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.commands.*;

import java.util.Optional;

public interface TripCommandService {
//    Optional<Trip> handle(CreateTripCommand command);
    void handle(DeleteTripCommand command);

    void handle(StartTripCommand command);
    void handle(FinishTripByClientCommand command);
    void handle(FinishTripByDriverCommand command);
//    void handle(FinishTripCommand command);
//    void handle(CancelTripCommand command);
    void handle(AddCommentCommand command);
}
