package com.techcompany.fastporte.users.domain.model.commands.client;

import jakarta.validation.constraints.NotBlank;

public record DeleteClientCommand(
        @NotBlank(message = "The client id is required") Long clientId
) {
}
