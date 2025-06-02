package com.techcompany.fastporte.users.domain.model.commands.driver;

import jakarta.validation.constraints.NotEmpty;

public record DeleteDriverExperienceCommand(
        @NotEmpty(message = "El id de la experiencia es requerido") Long experienceId
) {
}
