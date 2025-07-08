package com.techcompany.fastporte.trips.interfaces.rest.resources;

import com.techcompany.fastporte.shared.resources.summary.ClientSummaryResource;

public record RequestResource(
        Long id,
        ClientSummaryResource client,
        ServiceResource service,
        String status,
        TripInformationResource trip,
        ContractResource contract,
        boolean hasComment
) {
}