package com.techcompany.fastporte.users.domain.model.queries.client;

import jakarta.validation.constraints.NotBlank;

public record CheckClientExistsByIdQuery(
        @NotBlank(message = "Client id is required") Long clientId
) {
}
