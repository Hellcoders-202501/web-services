package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.application.dtos.TripInformationDto;
import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteTripCommand;

import java.util.Optional;

public interface TripCommandService {
    Optional<TripInformationDto> handle(CreateTripCommand command);
    void handle(DeleteTripCommand command);
}
