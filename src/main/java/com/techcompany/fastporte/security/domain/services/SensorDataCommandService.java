package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.commands.SaveSensorDataCommand;

public interface SensorDataCommandService {
    void handle(SaveSensorDataCommand command);
}
