package com.techcompany.fastporte.security.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.security.domain.model.commands.CreateAlertCommand;
import com.techcompany.fastporte.security.interfaces.rest.resources.CreateAlertResource;

import java.time.LocalDateTime;

public class CreateAlertCommandFromResourceAssembler {

    public static CreateAlertCommand fromResource(CreateAlertResource resource) {

        //Parsear el timestamp a LocalDateTime
        LocalDateTime timestamp = LocalDateTime.parse(resource.timestamp());

        return new CreateAlertCommand(
                resource.sensorType(),
                resource.value(),
                timestamp,
                resource.tripId()
        );
    }

}
