package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.application.dtos.TripCreatedDto;
import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteTripCommand;

import java.util.Optional;

public interface TripCommandService {
    Optional<TripCreatedDto> handle(CreateTripCommand command);
    void handle(DeleteTripCommand command);
}
