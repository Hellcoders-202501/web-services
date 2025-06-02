package com.techcompany.fastporte.users.domain.model.commands.client;

public record RegisterClientCommand(
        String name,
        String firstLastName,
        String secondLastName,
        String email,
        String phone,
        String password
) {
}