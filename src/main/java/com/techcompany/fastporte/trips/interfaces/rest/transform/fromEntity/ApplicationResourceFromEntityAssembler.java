package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.shared.transform.DriverSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplicationResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;

public class ApplicationResourceFromEntityAssembler {

    public static ApplicationResource toResourceFromEntity (Application application) {

        Driver driver = application.getDriver();
        Trip trip = application.getRequest().getTrip();

        return new ApplicationResource(
                application.getId(),
                DriverSummaryResourceFromEntityAssembler.assemble(driver),
                application.getMessage(),
                application.getProposedAmount(),
                TripInformationResourceFromEntityAssembler.toResourceFromEntity(trip)
        );
    }
}
