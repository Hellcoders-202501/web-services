package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.supervisor.RegisterSupervisorCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterSupervisorResource;

public class RegisterSupervisorCommandFromResourceAssembler {
    public static RegisterSupervisorCommand toCommandFromResource(RegisterSupervisorResource resource) {
        return new RegisterSupervisorCommand(
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
