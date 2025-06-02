package com.techcompany.fastporte.users.domain.services.driver;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Experience;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import com.techcompany.fastporte.users.domain.model.commands.driver.*;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface DriverCommandService {
    Optional<Driver> handle(RegisterDriverCommand command);
    Optional<Driver> handle(UpdateDriverInformationCommand command);
    void handle(DeleteDriverCommand command);
    Optional<Experience> handle (AddDriverExperienceCommand command);
    void handle(DeleteDriverExperienceCommand command);
    Optional<Vehicle> handle (AddDriverVehicleCommand command);
    void handle(DeleteDriverVehicleCommand command);
}
