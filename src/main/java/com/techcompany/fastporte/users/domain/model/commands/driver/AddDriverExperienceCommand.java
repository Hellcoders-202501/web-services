package com.techcompany.fastporte.users.domain.model.commands.driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AddDriverExperienceCommand (
        @NotEmpty(message = "El trabajo es requerido") String job,
        @NotBlank(message = "El tiempo es requerido") Double duration,
        @NotBlank(message = "El id del conductor es requerido") Long id
) {}
