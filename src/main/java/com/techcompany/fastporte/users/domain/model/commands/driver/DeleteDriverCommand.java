package com.techcompany.fastporte.users.domain.model.commands.driver;

import jakarta.validation.constraints.NotBlank;

public record DeleteDriverCommand(
        @NotBlank(message = "The driver id is required") Long driverId
) {
}
