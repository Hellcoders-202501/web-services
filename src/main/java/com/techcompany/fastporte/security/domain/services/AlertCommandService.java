package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.domain.model.commands.CreateAlertCommand;

import java.util.Optional;

public interface AlertCommandService {
    Optional<Alert> handle(CreateAlertCommand command);
}
