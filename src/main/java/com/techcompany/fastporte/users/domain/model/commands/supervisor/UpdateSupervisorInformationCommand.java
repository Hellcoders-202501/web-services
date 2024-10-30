package com.techcompany.fastporte.users.domain.model.commands.supervisor;

public record UpdateSupervisorInformationCommand(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String email,
        String password,
        String phone
) {
}
