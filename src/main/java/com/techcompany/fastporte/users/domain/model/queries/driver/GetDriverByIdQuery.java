package com.techcompany.fastporte.users.domain.model.queries.driver;

import jakarta.validation.constraints.NotBlank;

public record GetDriverByIdQuery(
       @NotBlank(message = "The driver id is required") Long id) {
}
