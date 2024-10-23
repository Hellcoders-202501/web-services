package com.techcompany.fastporte.users.domain.services.driver;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.RegisterDriverCommand;

import java.util.Optional;

public interface DriverCommandService {
    Optional<Driver> handle(RegisterDriverCommand command);
    void handle(DeleteDriverCommand command);
}
