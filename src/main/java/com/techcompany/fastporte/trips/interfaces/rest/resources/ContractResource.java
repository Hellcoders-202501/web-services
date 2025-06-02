package com.techcompany.fastporte.trips.interfaces.rest.resources;

import com.techcompany.fastporte.shared.resources.summary.DriverSummaryResource;

public record ContractResource(
        Long id,
        DriverSummaryResource driver,
        PaymentResource payment
) {
}
