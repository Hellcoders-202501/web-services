package com.techcompany.fastporte.users.domain.model.commands.supervisor;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record RegisterSupervisorCommand(
        String name,
        String firstLastName,
        String secondLastName,
        String email,
        String phone,
        String username,
        String password,
        String sensorCode
) {
}
