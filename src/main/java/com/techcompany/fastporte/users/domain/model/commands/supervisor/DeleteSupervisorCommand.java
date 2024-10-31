package com.techcompany.fastporte.users.domain.model.commands.supervisor;

import jakarta.validation.constraints.NotBlank;

public record DeleteSupervisorCommand (
        @NotBlank(message = "The supervisor id is required") Long supervisorId
) {
}
