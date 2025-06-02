package com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.trips.domain.model.commands.ApplyToRequestCommand;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplyToRequestResource;

public class ApplyToRequestCommandFromResourceAssembler {

    public static ApplyToRequestCommand toCommandFromResource(ApplyToRequestResource resource) {

        return new ApplyToRequestCommand(
                resource.message(),
                resource.proposedAmount(),
                resource.requestId(),
                resource.driverId()
        );
    }

}
