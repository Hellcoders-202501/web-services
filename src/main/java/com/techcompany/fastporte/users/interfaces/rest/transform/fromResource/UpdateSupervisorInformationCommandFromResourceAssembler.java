package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.supervisor.UpdateSupervisorInformationCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.UpdateSupervisorInformationResource;

public class UpdateSupervisorInformationCommandFromResourceAssembler {
    public static UpdateSupervisorInformationCommand toCommandFromResource(UpdateSupervisorInformationResource resource) {
        return new UpdateSupervisorInformationCommand(
                resource.id(),
                resource.name(),
                resource.firstLastName(),
                resource.secondLastName(),
                resource.email(),
                resource.password(),
                resource.phone()
        );
    }
}
