package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.client.RegisterClientCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterClientResource;

public class RegisterClientCommandFromResourceAssembler {
    public static RegisterClientCommand toCommandFromResource(RegisterClientResource resource) {
        return new RegisterClientCommand(
                resource.name(),
                resource.firstLastName(),
                resource.secondLastName(),
                resource.email(),
                resource.phone(),
                resource.username(),
                resource.password()
        );
    }
}
