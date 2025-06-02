package com.techcompany.fastporte.shared.resources.summary;

public record ClientSummaryResource(
        Long id,
        String name,
        String firstLastName,
        String imageUrl,
        String phone
) {
}
