package com.techcompany.fastporte.shared.resources.summary;

public record ApplicationSummaryResource(
        Long id,
        DriverSummaryResource driver,
        String message,
        Double proposedAmount
) {
}
