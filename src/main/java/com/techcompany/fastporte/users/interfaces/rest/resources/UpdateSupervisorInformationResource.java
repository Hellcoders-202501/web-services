package com.techcompany.fastporte.users.interfaces.rest.resources;

public record UpdateSupervisorInformationResource(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String email,
        String password,
        String phone
) {
}
