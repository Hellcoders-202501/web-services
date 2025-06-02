package com.techcompany.fastporte.shared.resources.summary;

public record DriverSummaryResource (
        Long id,
        String name,
        String firstLastName,
        String imageUrl,
        String phone,
        Double rating
) {
}
