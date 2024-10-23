package com.techcompany.fastporte.users.domain.model.queries.driver;

import jakarta.validation.constraints.NotBlank;

public record CheckDriverExistsByIdQuery(
        @NotBlank(message = "Driver id is required") String driverId
) {
}
