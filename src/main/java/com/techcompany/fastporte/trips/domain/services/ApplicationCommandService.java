package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.commands.ApplyToRequestCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteApplicationCommand;

public interface ApplicationCommandService {
    void handle(ApplyToRequestCommand command);
    void handle(DeleteApplicationCommand command);
}
