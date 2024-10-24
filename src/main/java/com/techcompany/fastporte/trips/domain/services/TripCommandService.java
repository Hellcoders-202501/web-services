package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteTripCommand;

import java.util.Optional;

public interface TripCommandService {
    Optional<Trip> handle(CreateTripCommand command);
    void handle(DeleteTripCommand command);
}
