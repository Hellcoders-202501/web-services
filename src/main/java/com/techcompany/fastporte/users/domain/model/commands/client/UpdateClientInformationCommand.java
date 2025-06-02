package com.techcompany.fastporte.users.domain.model.commands.client;

public record UpdateClientInformationCommand(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String phone,
        String description
) {
}
