package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record AddCommentResource(
        String content,
        Integer rating,
        Long tripId
) {
}
