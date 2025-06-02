package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.commands.ApplyToRequestCommand;

public interface ApplicationCommandService {
    void handle(ApplyToRequestCommand command);
}
