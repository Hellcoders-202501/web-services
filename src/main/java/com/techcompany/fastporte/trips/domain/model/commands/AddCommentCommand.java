package com.techcompany.fastporte.trips.domain.model.commands;

public record AddCommentCommand(
        String content,
        Integer rating,
        Long tripId
) {
}
