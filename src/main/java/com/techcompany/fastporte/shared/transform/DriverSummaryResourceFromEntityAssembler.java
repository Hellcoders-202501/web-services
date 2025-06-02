package com.techcompany.fastporte.shared.transform;

import com.techcompany.fastporte.shared.resources.summary.DriverSummaryResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;

public class DriverSummaryResourceFromEntityAssembler {

    public static DriverSummaryResource assemble(Driver driver) {

        return new DriverSummaryResource(
                driver.getId(),
                driver.getUser().getName(),
                driver.getUser().getFirstLastName(),
                driver.getUser().getImageUrl(),
                driver.getUser().getPhone(),
                driver.getRating()
        );
    }
}
