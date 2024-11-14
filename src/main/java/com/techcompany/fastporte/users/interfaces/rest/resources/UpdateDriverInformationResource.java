package com.techcompany.fastporte.users.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateDriverInformationResource(
        @NotBlank(message = "Driver id is required") Long id,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "First last name is required") String firstLastName,
        @NotBlank(message = "Second last name is required") String secondLastName,
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Phone is required") String phone,
        String password
) {
}
