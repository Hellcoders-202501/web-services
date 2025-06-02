package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.driver.AddDriverVehicleCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.AddDriverVehicleResource;

public class AddDriverVehicleCommandFromResourceAssembler {
    public static AddDriverVehicleCommand toCommandFromResource(AddDriverVehicleResource resource) {
        return new AddDriverVehicleCommand(
                resource.brand(),
                resource.imageUrl(),
                resource.serviceId(),
                resource.driverId()
        );
    }
}
