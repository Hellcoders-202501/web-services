package com.techcompany.fastporte.trips.interfaces.rest.resources;

import com.techcompany.fastporte.shared.resources.summary.ApplicationSummaryResource;

import java.util.List;

public record ApplicationsListResource(
        TripInformationResource trip,
        List<ApplicationSummaryResource> applications
) {
}
