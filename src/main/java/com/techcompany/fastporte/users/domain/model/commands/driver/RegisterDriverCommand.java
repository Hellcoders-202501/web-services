package com.techcompany.fastporte.users.domain.model.commands.driver;

import jakarta.validation.constraints.NotBlank;

public record RegisterDriverCommand(
    String name,
    String firstLastName,
    String secondLastName,
    String email,
    String phone,
    String username,
    String password,
    //Long clientId,
    String plate
    ) {

}
