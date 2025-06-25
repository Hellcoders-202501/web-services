package com.techcompany.fastporte.users.interfaces.rest.resources;

import com.techcompany.fastporte.shared.resources.summary.ClientSummaryResource;

public record DriverCommentResource(
        Long id,
        String content,
        Integer rating,
        ClientSummaryResource client
) {
}
