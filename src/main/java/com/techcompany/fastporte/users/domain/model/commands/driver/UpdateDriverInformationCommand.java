package com.techcompany.fastporte.users.domain.model.commands.driver;

public record UpdateDriverInformationCommand(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String phone,
        String description
) {
}
