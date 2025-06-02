package com.techcompany.fastporte.users.interfaces.rest.resources;

public record DriverInformationResource(
    Long id,
    String name,
    String firstLastName,
    String secondLastName,
    String email,
    String phone,
    String description,
    Long userId
) {
}
