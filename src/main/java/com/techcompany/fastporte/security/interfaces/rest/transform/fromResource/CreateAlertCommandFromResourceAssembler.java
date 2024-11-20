package com.techcompany.fastporte.security.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.security.domain.model.commands.CreateAlertCommand;
import com.techcompany.fastporte.security.interfaces.rest.resources.CreateAlertResource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CreateAlertCommandFromResourceAssembler {

    public static CreateAlertCommand fromResource(CreateAlertResource resource) {

        //Parsear el timestamp a LocalDateTime
        //LocalDateTime timestamp = LocalDateTime.parse(resource.timestamp());

        // Obtén la zona horaria de Perú
        ZoneId peruZone = ZoneId.of("America/Lima");
        ZonedDateTime peruTime = ZonedDateTime.now(peruZone);
        LocalDateTime now = peruTime.toLocalDateTime();

        return new CreateAlertCommand(
                resource.sensorType(),
                resource.value(),
                now,
                resource.tripId()
        );
    }

}
