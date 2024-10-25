package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.driver.RegisterDriverCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterDriverResource;

public class RegisterDriverCommandFromResourceAssembler {
    public static RegisterDriverCommand toCommandFromResource(RegisterDriverResource resource) {
        return new RegisterDriverCommand(
                resource.name(),
                resource.firstLastName(),
                resource.secondLastName(),
                resource.email(),
                resource.phone(),
                resource.username(),
                resource.password(),
                resource.plate()
        );
    }
}
