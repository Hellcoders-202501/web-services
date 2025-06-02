package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteRequestCommand;
import com.techcompany.fastporte.trips.domain.model.commands.PublishRequestCommand;

import java.util.Optional;

public interface RequestCommandService {
    Optional<Request> handle(PublishRequestCommand command);
    void handle(DeleteRequestCommand command);
}
