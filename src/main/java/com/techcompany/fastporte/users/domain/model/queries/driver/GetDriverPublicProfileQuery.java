package com.techcompany.fastporte.users.domain.model.queries.driver;

import jakarta.validation.constraints.NotBlank;

public record GetDriverPublicProfileQuery(
        @NotBlank(message = "The driver id is required") Long id
) {
}
