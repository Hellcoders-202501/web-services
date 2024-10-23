package com.techcompany.fastporte.users.domain.model.commands.supervisor;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record RegisterSupervisorCommand(
        @NotBlank(message = "The name is required") String name,
        @NotBlank(message = "The first last name is required") String firstLastName,
        @NotBlank(message = "The second last name is required") String secondLastName,
        @NotBlank(message = "The email is required") String email,
        @NotBlank(message = "The username is required") String username,
        @NotBlank(message = "The password is required") String password
) {
}
