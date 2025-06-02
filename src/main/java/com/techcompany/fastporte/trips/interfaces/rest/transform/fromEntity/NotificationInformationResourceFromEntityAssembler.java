package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.interfaces.rest.resources.NotificationInformationResource;

public class NotificationInformationResourceFromEntityAssembler {

    public static NotificationInformationResource fromEntity(Notification notificationInformation) {
        return new NotificationInformationResource(
            notificationInformation.getId(),
            notificationInformation.getCreatedAt(),
            notificationInformation.getType(),
            notificationInformation.isSeen(),
            notificationInformation.getUser().getId(),
            notificationInformation.getReferenceId()
        );
    }

}
