package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ServiceResource;

public class ServiceResourceFromEntityAssembler {

    public static ServiceResource toResourceFromEntity(Service service) {
        return new ServiceResource(
                service.getId(),
                service.getNameSpanish()
        );
    }
}