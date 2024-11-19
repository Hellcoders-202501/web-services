package com.techcompany.fastporte.security.domain.services;

import com.techcompany.fastporte.security.domain.model.commands.SaveRealTimeSensorDataCommand;

public interface RealTimeSensorDataCommandService {
    void handle(SaveRealTimeSensorDataCommand command);
}
