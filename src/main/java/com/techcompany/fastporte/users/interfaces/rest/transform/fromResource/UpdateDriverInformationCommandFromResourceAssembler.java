package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.driver.UpdateDriverInformationCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.UpdateDriverInformationResource;

public class UpdateDriverInformationCommandFromResourceAssembler {

    public static UpdateDriverInformationCommand toCommandFromResource(UpdateDriverInformationResource resource) {
        return new UpdateDriverInformationCommand(
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
