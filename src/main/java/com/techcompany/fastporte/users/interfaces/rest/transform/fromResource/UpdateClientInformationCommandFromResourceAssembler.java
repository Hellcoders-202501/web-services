package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.client.UpdateClientInformationCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.UpdateClientInformationResource;

public class UpdateClientInformationCommandFromResourceAssembler {
    public static UpdateClientInformationCommand toCommandFromResource(UpdateClientInformationResource resource) {
        return new UpdateClientInformationCommand(
                resource.id(),
                resource.name(),
                resource.firstLastName(),
                resource.secondLastName(),
                resource.phone(),
                resource.description()
        );
    }
}
