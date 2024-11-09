package com.techcompany.fastporte.users.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RegisterSupervisorResource (
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "First last name is required") String firstLastName,
    @NotBlank(message = "Second last name is required") String secondLastName,
    @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Phone is required") String phone,
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") String password,
    @NotBlank(message = "Sensor code is required") String sensorCode
) {

}
