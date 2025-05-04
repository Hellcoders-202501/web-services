package com.techcompany.fastporte.users.domain.model.queries.client;

import jakarta.validation.constraints.NotBlank;

public record GetClientByIdQuery(
        @NotBlank(message = "The client id is required") Long id
) {
}
