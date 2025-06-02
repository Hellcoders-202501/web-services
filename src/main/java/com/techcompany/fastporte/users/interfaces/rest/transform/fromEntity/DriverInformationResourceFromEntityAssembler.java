package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.interfaces.rest.resources.DriverInformationResource;

public class DriverInformationResourceFromEntityAssembler {

    public static DriverInformationResource toPublicResourceFromEntity(Driver driver) {
        return new DriverInformationResource(
                driver.getId(),
                driver.getUser().getName(),
                driver.getUser().getFirstLastName(),
                driver.getUser().getSecondLastName(),
                driver.getUser().getEmail(),
                driver.getUser().getPhone(),
                driver.getUser().getDescription(),
                driver.getUser().getId()
        );
    }
}
