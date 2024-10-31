package com.techcompany.fastporte.users.domain.model.queries.supervisor;

import jakarta.validation.constraints.NotBlank;

public record GetSupervisorByIdQuery(
        @NotBlank(message = "The supervisor id is required") Long id
) {
}
