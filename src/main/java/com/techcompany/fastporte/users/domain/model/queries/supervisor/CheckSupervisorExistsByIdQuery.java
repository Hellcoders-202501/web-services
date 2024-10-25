package com.techcompany.fastporte.users.domain.model.queries.supervisor;

import jakarta.validation.constraints.NotBlank;

public record CheckSupervisorExistsByIdQuery(
        @NotBlank(message = "Supervisor id is required") Long supervisorId
) {
}
