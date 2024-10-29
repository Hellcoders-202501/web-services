package com.techcompany.fastporte.security.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.interfaces.rest.resources.AlertInformationResource;
import com.techcompany.fastporte.security.interfaces.rest.resources.CreateAlertResource;

public class AlertInformationResourceFromEntityAssembler {

    public static AlertInformationResource toResourceFromEntity(Alert alert) {

        //Convertir la hora (LocalDateTime) a String
        String date = alert.getSensorData().getTimestamp().toLocalDate().toString();
        String time = alert.getSensorData().getTimestamp().toLocalTime().toString();

        CreateAlertResource createAlertResource = new CreateAlertResource(
                alert.getSensorData().getSensorType().getType().name(),
                alert.getSensorData().getValue(),
                date + " " + time,
                alert.getSensorData().getTripId()
        );

        return new AlertInformationResource(
                alert.getId(),
                alert.getAlertLevel().toString(),
                alert.getMessage(),
                createAlertResource
        );
    }
}