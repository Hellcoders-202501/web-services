package com.techcompany.fastporte.users.interfaces.rest.resources;

public record UpdateDriverInformationResource(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String phone,
        String description
) {
}
