package com.techcompany.fastporte.trips.interfaces.rest.resources;

import com.techcompany.fastporte.shared.resources.summary.DriverSummaryResource;

public record ApplicationResource(
        Long id,
        DriverSummaryResource driver,
        String message,
        Double proposedAmount,
        TripInformationResource trip
) {
}
