package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.shared.resources.summary.ApplicationSummaryResource;
import com.techcompany.fastporte.shared.transform.DriverSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplicationResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplicationsListResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;

import java.util.ArrayList;
import java.util.List;

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


    public static ApplicationsListResource toResourceFromEntity(List<Application> applications){

        Trip trip = applications.get(0).getRequest().getTrip();

        List<ApplicationSummaryResource> applicationsList = new ArrayList<>();

        for (Application application : applications) {

            applicationsList.add(
                    new ApplicationSummaryResource(
                            application.getId(),
                            DriverSummaryResourceFromEntityAssembler.assemble(application.getDriver()),
                            application.getMessage(),
                            application.getProposedAmount()
                    )
            );
        }

        return new ApplicationsListResource(
                TripInformationResourceFromEntityAssembler.toResourceFromEntity(trip),
                applicationsList
        );
    }
}
