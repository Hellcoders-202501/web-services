package com.techcompany.fastporte.users.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDriverResource(
    @NotBlank(message = "The name is required") String name,
    @NotBlank(message = "The first last name is required") String firstLastName,
    @NotBlank(message = "The second last name is required") String secondLastName,
    @NotBlank(message = "The email is required") String email,
    @NotBlank(message = "The phone is required") String phone,
    //@NotBlank(message = "The email is required") String email,
    @NotBlank(message = "The password is required") String password
) {
}
