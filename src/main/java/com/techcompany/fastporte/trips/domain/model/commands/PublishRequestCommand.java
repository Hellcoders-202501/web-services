package com.techcompany.fastporte.trips.domain.model.commands;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;

public record PublishRequestCommand(
        Long clientId,
        Long serviceId,
        Trip trip
) {
}
